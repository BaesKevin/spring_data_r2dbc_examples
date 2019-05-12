package be.kevinbaes.bap.springdata.r2dbc.persistence.repository;

import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GoalRepository extends R2dbcRepository<Goal, Integer> {

    @Query("SELECT * FROM goal WHERE NAME LIKE $1")
    Flux<Goal> findByNameLike(String name);

//    Query derivation not yet supported!
//    Flux<Goal> findByNameContaining(String name);



}
