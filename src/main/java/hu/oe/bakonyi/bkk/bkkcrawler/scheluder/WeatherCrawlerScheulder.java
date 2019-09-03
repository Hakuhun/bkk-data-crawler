package hu.oe.bakonyi.bkk.bkkcrawler.scheluder;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Log4j2
@Component
public class WeatherCrawlerScheulder {



    @Scheduled(cron = "${scheulder.testScheulder}")
    public void downloadWeatherData(){
        System.out.println("ASDASDASDASDASDASD ");
        log.info("Weather geci: PINA");
    }

}
