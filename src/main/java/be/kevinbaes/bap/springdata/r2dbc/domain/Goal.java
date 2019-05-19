package be.kevinbaes.bap.springdata.r2dbc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Goal {
  @Id
  private Integer id;
  private String name;

  public Goal(String name) {
    this.name = name;
  }
}
