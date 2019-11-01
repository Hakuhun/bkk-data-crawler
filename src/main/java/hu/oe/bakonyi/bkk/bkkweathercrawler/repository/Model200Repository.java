package hu.oe.bakonyi.bkk.bkkweathercrawler.repository;

import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Model200;
import org.springframework.data.repository.CrudRepository;

public interface Model200Repository extends CrudRepository<Model200, Integer> {
    Model200 findByCoordLatAndCoordLon(double lat, double lon);
    Model200 findByCoord(Coord coord);
}
