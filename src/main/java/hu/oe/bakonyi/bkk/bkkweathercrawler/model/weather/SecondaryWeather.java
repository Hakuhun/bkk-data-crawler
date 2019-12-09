package hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecondaryWeather {

    private List<SecondaryWeatherData> data;

    @Data
    public static class SecondaryWeatherData{
        private double snow;
        private double precip;
        private double vis;
    }
}
