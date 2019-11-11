package hu.oe.bakonyi.bkk.bkkweathercrawler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import hu.oe.bakonyi.bkk.bkkweathercrawler.repository.Model200Repository;
import hu.oe.bakonyi.bkk.bkkweathercrawler.service.DefaultCoordinateService;
import hu.oe.bakonyi.bkk.bkkweathercrawler.service.MapDetailsService;
import hu.oe.bakonyi.bkk.bkkweathercrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController("weather")
public class WeatherController {

    @Autowired
    MapDetailsService service;

    @Autowired
    WeatherConfiguration configuration;

    @Autowired
    DefaultCoordinateService coordinateService;

    @Autowired
    Model200Repository repository;

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/prod/weather")
    public ResponseEntity<Model200> getWeatherByCoordinate(@RequestBody Coord coordinate){
        Coord nearestCoord = coordinateService.getNearestCoordToChunk(coordinate);
        Model200 weather1 = repository.findByCoordLatAndCoordLon(nearestCoord.getLat(), nearestCoord.getLon());

        if (weather1 != null){
                return ResponseEntity.ok(weather1);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/Dev/weathers")
    public ResponseEntity getWeathers(@RequestParam("time") String time){
        try {
            Instant nodeFileTime = Instant.ofEpochSecond(Long.parseLong(time));
            if (nodeFileTime.isBefore(service.getLastModoficationTime()))
            {
                List<Model200> list = Arrays.asList(mapper.readValue(new File(configuration.getPathToFile()), Model200[].class));
                log.info("Erőőforrás lekérdezés történt: /bkk/weather", list);
                return ResponseEntity.ok(list);
            }else{
                log.info("Nem történt erőforrás kiszolgálás, azt átadott idő paraméter nem volt kissebb, mint a legkésőbbi file.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A kért erőforrás - weatherproperties.json - nem áll rendelkezésre");
        }
    }

    //http://localhost:8001/bkk/lastModified
    @GetMapping("/lastModified")
    public ResponseEntity getModifyTime(){
        try {
            Instant creationTime = service.getLastModoficationTime();
            log.info("Utolsó modosítási idő lekérdezés történt. Modosítási idő: " + creationTime.getEpochSecond());
            return ResponseEntity.ok(mapper.writeValueAsString(creationTime.getEpochSecond()));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
