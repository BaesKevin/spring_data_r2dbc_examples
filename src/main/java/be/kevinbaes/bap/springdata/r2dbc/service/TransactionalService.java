package be.kevinbaes.bap.springdata.r2dbc.service;

import be.kevinbaes.bap.springdata.r2dbc.domain.FrownedUponException;
import be.kevinbaes.bap.springdata.r2dbc.domain.Goal;
import be.kevinbaes.bap.springdata.r2dbc.persistence.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionalService {

  private final GoalRepository goalRepository;

  @Transactional
  public Mono<Void> saveAll(String... names) {
    Mono<Void> complete = Mono.empty();

    for(String name : names) {
      if(name.equals("start doing drugs")) {
        complete = complete.then(Mono.error(new FrownedUponException("The goal '" + name + "' is frowned upon")));
      } else {
        complete = complete.then(goalRepository.save(new Goal(name)).then());
      }
    }

    return complete;
  }

}
