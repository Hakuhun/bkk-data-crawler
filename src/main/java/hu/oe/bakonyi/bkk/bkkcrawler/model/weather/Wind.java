package hu.oe.bakonyi.bkk.bkkcrawler.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Wind
 */
@Validated
@Data
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-02T19:31:53.527Z[GMT]")
public class Wind   {
  @JsonProperty("speed")
  private BigDecimal speed = null;

  @JsonProperty("deg")
  private Integer deg = null;

}
