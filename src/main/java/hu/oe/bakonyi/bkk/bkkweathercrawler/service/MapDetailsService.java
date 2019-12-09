package hu.oe.bakonyi.bkk.bkkweathercrawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.client.SecondaryWeatherSourceClient;
import hu.oe.bakonyi.bkk.bkkweathercrawler.client.WeatherClient;
import hu.oe.bakonyi.bkk.bkkweathercrawler.configuration.WeatherConfiguration;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.*;
import hu.oe.bakonyi.bkk.bkkweathercrawler.repository.Model200Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

    @Autowired
    SecondaryWeatherSourceClient secondaryClient;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Model200Repository repository;

    public Instant getLastModoficationTime() throws IOException {
        File weatherFile = new File(configuration.getPathToFile());
        FileTime creationTime;
        BasicFileAttributes attributes = Files.readAttributes(weatherFile.toPath(),BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        return attributes.creationTime().toInstant();
    }

    public List<Model200> calculateChunks(){
        List<Model200> weathers = new ArrayList<>();
        List<Coord> coords = new ArrayList<>();

        for (int i = 0; i < configuration.getChunkWideSize(); i++){
            double lat = configuration.getBottomLeftLat() + (i * getDiffLat());
            for (int j = 0; j <configuration.getChunkHighSize(); j++){
                double lng = configuration.getBottomLeftLong() + (j * getDiffLong());
                Model200 weather = client.getWeather("metric", configuration.getApiKey(), lat, lng);
                weathers.add(weather);
                coords.add(weather.getCoord());
                repository.save(weather);
            }
        }

        double precip = 0.0;
        double snow = 0.0;
        double visibility = 0.0;

        for (int i = 0; i < weathers.size(); i++){
            Model200 currentWeather = weathers.get(i);
            if(i % 3 == 0){
                double lat = currentWeather.getCoord().getLat();
                double lng = currentWeather.getCoord().getLon();
                SecondaryWeather secondaryWeather = secondaryClient.getWeatherByGeolocation(configuration.getSecondaryApiKey(), lat, lng);
                precip = secondaryWeather.getData().stream().max(Comparator.comparingDouble(SecondaryWeather.SecondaryWeatherData::getPrecip)).get().getPrecip();
                snow = secondaryWeather.getData().stream().max(Comparator.comparingDouble(SecondaryWeather.SecondaryWeatherData::getSnow)).get().getSnow();
                visibility = secondaryWeather.getData().stream().max(Comparator.comparingDouble(SecondaryWeather.SecondaryWeatherData::getVis)).get().getVis();
            }

            currentWeather.setSnow(getMaxSnow(currentWeather, snow));
            currentWeather.setRain(getMaxPrecip(currentWeather, precip));
            currentWeather.setVisibility((int) getMaxVisibility(currentWeather, visibility));
            weathers.set(i, currentWeather);
            repository.save(currentWeather);
        }

        try {
            mapper.writeValue(new File("weathercoords.json"), coords);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weathers;
    }

    private double getDiffLat() {
        return Math.abs(configuration.getTopRightLat() - configuration.getBottomLeftLat()) / configuration.getChunkWideSize();
    }

    private double getDiffLong(){
        return Math.abs(configuration.getTopRightLong() - configuration.getBottomLeftLong()) / configuration.getChunkHighSize();
    }

    private Rain getMaxPrecip(Model200 currentW, double b){
        BigDecimal a = BigDecimal.valueOf(0);

        if(currentW.getRain() != null && currentW.getRain().get_3h() != null){
            a = currentW.getRain().get_3h();
        }else{
            a = BigDecimal.valueOf(0);
        }

        if (a.doubleValue() != b){
            return Rain.builder()._3h(BigDecimal.valueOf(Math.max(a.doubleValue(), b))).build();
        }else{
            return Rain.builder()._3h(a).build();
        }
    }

    private Snow getMaxSnow(Model200 currentW, double b){
        BigDecimal a = BigDecimal.valueOf(0);

        if(currentW.getSnow() != null && currentW.getSnow().get_3h() != null){
            a = currentW.getRain().get_3h();
        }else{
            a = BigDecimal.valueOf(0);
        }

        if (a.doubleValue() != b){
            return Snow.builder()._3h(BigDecimal.valueOf(Math.max(a.doubleValue(), b))).build();
        }else{
            return Snow.builder()._3h(a).build();
        }
    }

    private double getMaxVisibility(Model200 currentW, double b){
        double a = 0;

        if(currentW.getVisibility() != null){
            a = currentW.getVisibility();
        }

        if (a != b){
            return Math.min(a, b);
        }else{
            return a;
        }
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
