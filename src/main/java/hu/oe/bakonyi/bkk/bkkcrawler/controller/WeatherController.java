package hu.oe.bakonyi.bkk.bkkcrawler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkcrawler.businesslogic.MapDetailsService;
import hu.oe.bakonyi.bkk.bkkcrawler.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/weather")
    public ResponseEntity getWeathers(){

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Model200> list = Arrays.asList(mapper.readValue(new File("${weather.pathToFile}"), Model200[].class));
            return ResponseEntity.ok(list);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A kért erőforrás - weatherproperties.json - nem áll rendelkezésre");
        }
    }

    //http://localhost:8001/bkk/lastModified
    @GetMapping("/lastModified")
    public ResponseEntity getModifyTime(){
        log.info("Something just happened 2");
        return ResponseEntity.ok(Instant.now());
    }
}
