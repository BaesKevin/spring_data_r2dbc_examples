package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualGoalRepositoryIntegrationTest {

  @Autowired
  private ManualGoalRepository goalRepository;

  @Autowired
  private TransactionalDatabaseClient databaseClient;

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
    Mono<Integer> insert = goalRepository.insert("insert in transaction 1");
    Mono<Integer> insert2 = goalRepository.insert("insert in transaction 2");
    Mono<Integer> insert3 = goalRepository.insert(null);

    Mono<Void> insertInTransaction = databaseClient
        .beginTransaction()
        .then(insert)
        .then(insert2)
        .then(insert3)
        .then(databaseClient.commitTransaction());

    insertInTransaction = databaseClient.enableTransactionSynchronization(insertInTransaction);

    StepVerifier
        .create(insertInTransaction)
        .verifyError();
  }


  @Test
  public void insertInTransactionCommit() {
    Mono<Integer> insert = goalRepository.insert("insert in transaction 1");
    Mono<Integer> insert2 = goalRepository.insert("insert in transaction 2");
    Mono<Integer> insert3 = goalRepository.insert("insert in transaction 3");

    Mono<Void> insertInTransaction = databaseClient
        .beginTransaction()
        .then(insert)
        .then(insert2)
        .then(insert3)
        .doOnEach(System.out::println)
        .then(databaseClient.commitTransaction());

    insertInTransaction = databaseClient.enableTransactionSynchronization(insertInTransaction);

    StepVerifier
        .create(insertInTransaction)
        .verifyComplete();
  }


  @Test
  public void save() {
    Goal goal = new Goal(0, "some goal");

    Mono<Goal> save = goalRepository.save(goal);

    StepVerifier
        .create(save)
        .assertNext(newGoal -> assertTrue(newGoal.getId() > 0))
        .verifyComplete();
  }
}