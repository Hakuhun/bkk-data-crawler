package hu.oe.bakonyi.bkk.bkkweathercrawler.client;

import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Model200;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name="weather-api",
    url = "${weather.apiUrl}"
)
public interface WeatherClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/weather"
    )
    Model200 getWeather(@RequestParam(value = "units") String units,
                        @RequestParam(value = "apiKey") String apiKey,
                        @RequestParam(value = "lat") double lat,
                        @RequestParam(value = "lon") double lon
                        );
}
