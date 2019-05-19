package be.kevinbaes.bap.springdata.r2dbc.service;

import be.kevinbaes.bap.springdata.r2dbc.persistence.repository.ManualGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionalService {

  private final ManualGoalRepository manualGoalRepository;

  @Transactional
  public Mono<Void> saveAll(String... names) {
    Mono<Void> complete = Mono.empty();

    for(String name : names) {
      if(name.equals("start doing drugs")) {
        complete = complete.then(Mono.error(new IllegalArgumentException("The goal '" + name + "' is frowned upon")));
      } else {
        complete = complete.then(manualGoalRepository.insert(name).then());
      }
    }

    return complete;
  }

}
