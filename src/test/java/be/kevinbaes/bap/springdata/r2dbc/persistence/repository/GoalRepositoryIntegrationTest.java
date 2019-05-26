package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GoalRepositoryIntegrationTest {

  private final GoalRepository goalRepository;

  public GoalRepositoryIntegrationTest() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

    this.goalRepository = ctx.getBean(GoalRepository.class);
  }

  /**
   * Uncoment the test and the query to see the 'query derivation not yet supported!' exception.
   */
//  @Test
//  public void findByName() {
//    Flux<Goal> goals = goalRepository.findByNameContaining("something");
//
//    List<Goal> goalList = goals.collectList().block();
//    assertNotNull(goalList);
//  }
}
