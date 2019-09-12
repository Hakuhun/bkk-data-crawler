package hu.oe.bakonyi.bkk.bkkweathercrawler.scheluder;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.businesslogic.MapDetailsService;
import hu.oe.bakonyi.bkk.bkkweathercrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Log4j2
@Component
public class WeatherCrawlerScheulder {

    @Autowired
    WeatherConfiguration configuration;

    @Autowired
    MapDetailsService service;

    @PostConstruct
    public void initialScheulde(){
        log.info("Inicializáló letöltés: ");
        download();
    }

    @Transient
    @Scheduled(cron = "${scheulder.weatherScheduler}")
    public void downloadWeatherData(){
        log.info("Kezdés: "+Instant.now().getEpochSecond());
        download();
    }

    public void download(){
        log.info("Időjárás adatok letöltése megkezdődött");
        List<Model200> weathers = service.calculateChunks();
        log.info("Időjárás adatok letöltve, mentésre előkészítve");
        log.info("Időjárás adatok");
        log.info(weathers);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(configuration.getPathToFile()), weathers);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
