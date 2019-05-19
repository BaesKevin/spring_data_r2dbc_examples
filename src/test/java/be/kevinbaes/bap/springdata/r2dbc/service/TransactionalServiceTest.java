package be.kevinbaes.bap.springdata.r2dbc.service;

import be.kevinbaes.bap.springdata.r2dbc.Application;
import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import be.kevinbaes.bap.springdata.r2dbc.persistence.repository.ManualGoalRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class TransactionalServiceTest {

  private final ReactiveTransactionManager transactionManager;
  private final TransactionalService transactionalService;
  private final DatabaseClient databaseClient;
  private final ManualGoalRepository goalRepository;


  public TransactionalServiceTest() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
    this.transactionManager = ctx.getBean(ReactiveTransactionManager.class);
    this.transactionalService = ctx.getBean(TransactionalService.class);
    this.databaseClient = ctx.getBean(DatabaseClient.class);
    this.goalRepository = ctx.getBean(ManualGoalRepository.class);
  }

  @Before
  public void before() {
    databaseClient.execute().sql("delete from goal;").fetch().all().blockLast();
  }

  @Test
  public void saveAll() {
    Flux<Goal> saveAll = transactionalService
        .saveAll("exercise", "write bachelor's thesis")
        .thenMany(goalRepository.findAll());

    StepVerifier
        .create(saveAll)
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  public void saveAllRollback() {
    Flux<Goal> saveAll = transactionalService
        .saveAll("exercise", "start doing drugs")
        .thenMany(goalRepository.findAll());

    StepVerifier
        .create(saveAll)
        .verifyError();
  }
}