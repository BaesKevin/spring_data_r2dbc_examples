package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.Application;
import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertTrue;

/**
 * @Transaction currently doesn't work in tests with SpringRunner because it looks for PlatformTransactionManager
 */
public class ManualGoalRepositoryIntegrationTest {

  private final ManualGoalRepository goalRepository;
  private final DatabaseClient databaseClient;
  private final ReactiveTransactionManager transactionManager;

  public ManualGoalRepositoryIntegrationTest() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

    this.goalRepository = ctx.getBean(ManualGoalRepository.class);
    this.databaseClient = ctx.getBean(DatabaseClient.class);
    this.transactionManager = ctx.getBean(ReactiveTransactionManager.class);
  }

  @Test
  public void findAllTest() {

    Mono<Void> findAll = goalRepository.findAll().doOnNext(System.out::println).then();

    StepVerifier
        .create(findAll)
        .verifyComplete();

  }

  @Test
  public void insert() {
    Mono<Integer> insert = goalRepository.insert("test goal");

    StepVerifier
        .create(insert)
        .expectNext(1)
        .verifyComplete();
  }

  @Test
  public void insertInTransactionRollback() {
    TransactionalOperator operator = TransactionalOperator.create(transactionManager);

    Mono<Integer> insert = goalRepository.insert("insert in transaction 1");
    Mono<Integer> insert2 = goalRepository.insert("insert in transaction 2");
    Mono<Integer> insert3 = goalRepository.insert("goal3")
        .concatWith(Flux.error(new IllegalArgumentException("can't use goal3")))
        .then(Mono.just(1));

    Mono<Void> insertInTransaction =
        insert
        .then(insert2)
        .then(insert3)
        .as(operator::transactional)
        .then();

    StepVerifier
        .create(insertInTransaction)
        .verifyError();
  }

  @Test
  public void insertInTransactionCommit() {
    TransactionalOperator operator = TransactionalOperator.create(transactionManager);

    Mono<Integer> insert = goalRepository.insert("goal1");
    Mono<Integer> insert2 = goalRepository.insert("goal2");
    Mono<Integer> insert3 = goalRepository.insert("goal3");

    Mono<Void> insertInTransaction =
        insert
        .then(insert2)
        .then(insert3)
        .as(operator::transactional)
        .doOnEach(System.out::println)
        .then();

    StepVerifier
        .create(insertInTransaction)
        .verifyComplete();
  }


  @Test
  public void save() {
    Goal goal = new Goal("some goal");

    Mono<Goal> save = goalRepository.save(goal);

    StepVerifier
        .create(save)
        .assertNext(newGoal -> assertTrue(newGoal.getId() > 0))
        .verifyComplete();
  }
}