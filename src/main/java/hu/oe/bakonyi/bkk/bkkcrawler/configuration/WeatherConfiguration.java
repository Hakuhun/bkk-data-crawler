package hu.oe.bakonyi.bkk.bkkcrawler.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(value = "weather")
public class WeatherConfiguration {
    private float topRightLat;
    private float topRightLong;
    private float bottomLeftLat;
    private float bottomLeftLong;

    private String apiKey;
    private String apiUrl;

    private int chunkWideSize;
    private int chunkHighSize;
}
