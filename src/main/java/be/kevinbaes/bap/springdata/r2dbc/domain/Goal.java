package be.kevinbaes.bap.springdata.r2dbc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Goal {
  @Id
  private int id;
  private String name;
}
