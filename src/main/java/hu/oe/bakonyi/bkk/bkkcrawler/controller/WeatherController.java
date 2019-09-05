package hu.oe.bakonyi.bkk.bkkcrawler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkcrawler.businesslogic.MapDetailsService;
import hu.oe.bakonyi.bkk.bkkcrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkcrawler.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/weather")
    public ResponseEntity getWeathers(@RequestParam("time") String time){
        try {
            Instant nodeFileTime = Instant.ofEpochSecond(Long.valueOf(time));
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
