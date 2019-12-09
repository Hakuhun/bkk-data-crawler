package hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Wind
 */
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-02T19:31:53.527Z[GMT]")
public class Wind   {
  @JsonProperty("speed")
  private BigDecimal speed = null;

  @JsonProperty("deg")
  private Integer deg = null;

}
