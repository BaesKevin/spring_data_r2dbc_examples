package be.kevinbaes.bap.springdata.jdbc.persistence;

import be.kevinbaes.bap.springdata.jdbc.domain.Goal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcGoalRepositoryTest {
  @Autowired
  private JdbcGoalRepository jdbcGoalRepository;

  @Test
  public void findByNameLike() {
    List<Goal> goals = jdbcGoalRepository.findByNameContaining("insert");

    assertEquals(3, goals.size());
  }
}