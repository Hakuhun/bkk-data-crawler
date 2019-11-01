package hu.oe.bakonyi.bkk.bkkweathercrawler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import hu.oe.bakonyi.bkk.bkkweathercrawler.repository.Model200Repository;
import net.sf.geographiclib.Geodesic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultCoordinateService {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    Model200Repository repository;

    public Coord getNearestCoordToChunk(Coord coord){
        Map<Double, Coord> coordDistanceMap = this.getDefaultCoordinates().stream().collect(Collectors.toMap((c) -> getDistance(coord, c), (c) -> c));
        return coordDistanceMap.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getKey)).get().getValue();
    }

    public List<Coord> getDefaultCoordinates(){
        List<Coord> coords = null;
        try {
            coords = Arrays.asList( mapper.readValue(new File("weathercoords.json"), Coord[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coords;
    }

    private double getDistance(Coord a, Coord b){
        return this.getDistance(a.getLat(), a.getLon(), b.getLat(), b.getLon());
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        Geodesic geod = Geodesic.WGS84;// This matches EPSG4326, which is the coordinate system used by Geolake
        return geod.InverseLine(lat1, lon1, lat2, lon2).Distance();
    }

}
