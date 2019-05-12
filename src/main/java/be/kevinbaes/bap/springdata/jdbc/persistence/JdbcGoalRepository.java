package be.kevinbaes.bap.springdata.jdbc.persistence;

import be.kevinbaes.bap.springdata.jdbc.domain.Goal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdbcGoalRepository extends CrudRepository<Goal, Integer> {
  List<Goal> findByNameContaining(String name);
}
