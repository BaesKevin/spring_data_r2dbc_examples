package be.kevinbaes.bap.springdata.r2dbc.service;

import be.kevinbaes.bap.springdata.r2dbc.Application;
import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import be.kevinbaes.bap.springdata.r2dbc.persistence.repository.GoalRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class TransactionalAnnotationNotSupportedTest {

  @Autowired
  private GoalRepository goalRepository;

  // Failed to retrieve PlatformTransactionManager
  @Test(expected = IllegalStateException.class)
  @Transactional
  public void transactionDoesNotWorkOnSpringRunnerTests() {
    Mono<Goal> goalMono = goalRepository.save(new Goal("should not work"));

    StepVerifier.create(goalMono)
        .expectNextCount(1)
        .verifyComplete();
  }
}
