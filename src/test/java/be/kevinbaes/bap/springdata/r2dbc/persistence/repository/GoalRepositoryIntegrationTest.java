package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoalRepositoryIntegrationTest {

  @Autowired
  private GoalRepository goalRepository;

  @Test
  public void findByName() {
    // Query derivation not yet supported!
    Flux<Goal> findByNameLike = goalRepository.findByNameLike("%insert%");

    StepVerifier
        .create(findByNameLike)
        .expectNextCount(3)
        .verifyComplete();
  }

}
