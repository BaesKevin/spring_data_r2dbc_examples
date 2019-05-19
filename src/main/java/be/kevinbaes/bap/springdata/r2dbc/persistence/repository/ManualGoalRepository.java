package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Github readme is the best we got for now https://github.com/spring-projects/spring-data-r2dbc
 */
@Repository
public class ManualGoalRepository {
  private final DatabaseClient client;

  public ManualGoalRepository(DatabaseClient transactionalDatabaseClient) {
    this.client = transactionalDatabaseClient;
  }

  public Flux<Goal> findAll() {
    return client
        .execute()
        .sql("select * from goal")
        .map((r, rm) -> new Goal(r.get("id", Integer.class), r.get("name", String.class)))
        .all();
  }

  public Mono<Integer> insert(String name) {
    return client
        .execute().sql("INSERT INTO goal (name) VALUES($1)") //
        .bind(0, name) //
        .fetch()
        .rowsUpdated();
  }

  public Mono<Integer> delete(int id) {
    return client.execute()
        .sql("delete from goal where id = $1")
        .bind(0, id)
        .fetch().rowsUpdated();
  }

  public Mono<Goal> findById(int id) {
    return client.execute()
        .sql("select * from goal where id = $1")
        .bind(0, id)
        .as(Goal.class)
        .fetch()
        .one();
  }

  public Mono<Goal> save(Goal goal) {

    return client.insert()
        .into(Goal.class)
        .using(goal)
        .map((row, rm) -> {
          Integer id = row.get("id", Integer.class);
          String name = row.get("name", String.class);

          if (id == null) {
            throw new IllegalArgumentException("column id is null");
          }
          return new Goal(id, name);
        }).first();
  }

}
