package hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SecondaryWeather {

    private List<SecondaryWeatherData> data;

    @Data
    public static class SecondaryWeatherData{
        private double snow;
        private double precip;
    }
}
