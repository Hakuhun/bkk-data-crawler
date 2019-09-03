package hu.oe.bakonyi.bkk.bkkcrawler.businesslogic.model;

import hu.oe.bakonyi.bkk.bkkcrawler.model.weather.Model200;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Chunk {
    private Location center;
    private Model200 weather;
}
