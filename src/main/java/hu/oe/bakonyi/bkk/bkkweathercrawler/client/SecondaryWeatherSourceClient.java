package hu.oe.bakonyi.bkk.bkkweathercrawler.client;

import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Model200;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.SecondaryWeather;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "secondary-weather-source",
        url = "${weather.secondaryBaseUrl}"
)
public interface SecondaryWeatherSourceClient  {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/current"
    )
    SecondaryWeather getWeatherByGeolocation(
                                @RequestParam(value = "key") String apiKey,
                                @RequestParam(value = "lat") double lat,
                                @RequestParam(value = "lon") double lon
    );

}
