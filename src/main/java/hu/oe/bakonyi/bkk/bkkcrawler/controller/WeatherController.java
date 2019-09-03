package hu.oe.bakonyi.bkk.bkkcrawler.controller;

import hu.oe.bakonyi.bkk.bkkcrawler.businesslogic.MapDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Log4j2
@RestController("weather")
public class WeatherController {

    @Autowired
    MapDetailsService service;

    @GetMapping("/weather")
    public ResponseEntity getWeathers(){
        log.info("Something just happened 1");
        return ResponseEntity.ok(service.calculateChunks());
    }

    //http://localhost:8001/bkk/lastModified
    @GetMapping("/lastModified")
    public ResponseEntity getModifyTime(){
        log.info("Something just happened 2");
        return ResponseEntity.ok(Instant.now());
    }
}
