package hu.oe.bakonyi.bkk.bkkcrawler.businesslogic;

import hu.oe.bakonyi.bkk.bkkcrawler.client.WeatherClient;
import hu.oe.bakonyi.bkk.bkkcrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkcrawler.model.weather.Model200;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.*;

@Service
public class MapDetailsService {

    @Autowired
    WeatherConfiguration configuration;

    @Autowired
    WeatherClient client;

    public Instant getLastModoficationTime() throws IOException {
        File weatherFile = new File(configuration.getPathToFile());
        FileTime creationTime;
        BasicFileAttributes attributes = Files.readAttributes(weatherFile.toPath(),BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        return attributes.creationTime().toInstant();
    }

    public List<Model200> calculateChunks(){
        List<Model200> weathers = new ArrayList<>();

        for (int i = 0; i < configuration.getChunkWideSize(); i++){
            double lat = configuration.getBottomLeftLat() + (i * getDiffLat());
            for (int j = 0; j <configuration.getChunkHighSize(); j++){
                double lng = configuration.getBottomLeftLong() + (j * getDiffLong());
                //Location loc = new Location(lat, lng);
                Model200 weather = client.getWeather("metric", configuration.getApiKey(), lat, lng);
                weathers.add(weather);
            }
        }

        return weathers;
    }

    private double getDiffLat() {
        return Math.abs(configuration.getTopRightLat() - configuration.getBottomLeftLat()) / configuration.getChunkWideSize();
    }

    private double getDiffLong(){
        return Math.abs(configuration.getTopRightLong() - configuration.getBottomLeftLong()) / configuration.getChunkHighSize();
    }
/*
    private double getDistance(Location a, Location b){
        return this.getDistance(a.getLat(), a.getLng(), b.getLat(), b.getLng());
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        Geodesic geod = Geodesic.WGS84;// This matches EPSG4326, which is the coordinate system used by Geolake
        return geod.InverseLine(lat1, lon1, lat2, lon2).Distance();
    }

    private Chunk MinimalDistance(Location veichleLocation)
    {
        Map<Double, Chunk> distances = new HashMap<>();

        for(Chunk chunk : this.calculateChunks())
        {
            distances.put(getDistance(chunk.getCenter(), veichleLocation), chunk);
        }

        double min = Integer.MAX_VALUE;

        for (Map.Entry<Double, Chunk> entry : distances.entrySet()) {
            Double key = entry.getKey();
            Chunk value = entry.getValue();

            if (min > key)
            {
                min = key;
            }
        }

        return distances.get(min);
    }
*/
}
