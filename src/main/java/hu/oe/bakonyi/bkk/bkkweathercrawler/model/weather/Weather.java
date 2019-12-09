package hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-09-02T19:31:53.527Z[GMT]")
public class Weather   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("main")
  private String main = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("icon")
  private String icon = null;
}
