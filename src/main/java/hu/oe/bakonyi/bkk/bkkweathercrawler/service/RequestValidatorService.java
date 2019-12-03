package hu.oe.bakonyi.bkk.bkkweathercrawler.service;

import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RequestValidatorService {

    public void validateCoord(Coord coord){
        if(coord == null || coord.getLat() == 0 ||coord.getLon() == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
