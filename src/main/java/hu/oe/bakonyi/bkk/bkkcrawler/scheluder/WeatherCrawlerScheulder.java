package hu.oe.bakonyi.bkk.bkkcrawler.scheluder;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkcrawler.businesslogic.MapDetailsService;
import hu.oe.bakonyi.bkk.bkkcrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkcrawler.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Log4j2
@Component
public class WeatherCrawlerScheulder {

    @Autowired
    WeatherConfiguration configuration;

    @Autowired
    MapDetailsService service;

    @Transient
    @Scheduled(cron = "${scheulder.weatherScheduler}")
    public void downloadWeatherData(){
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
