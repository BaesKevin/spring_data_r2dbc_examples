package be.kevinbaes.bap.springdata.r2dbc;

import be.kevinbaes.bap.springdata.r2dbc.persistence.repository.ManualGoalRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

        ManualGoalRepository goalRepository = ctx.getBean(ManualGoalRepository.class);

        Mono<Integer> insert = goalRepository.insert("insert in transaction 1");
        Mono<Integer> insert2 = goalRepository.insert("insert in transaction 2");
        Mono<Integer> insert3 = goalRepository.insert("insert in transaction 3");

        Mono<Void> insertInTransaction =
            insert
                .then(insert2)
                .then(insert3)
                .then();

        insertInTransaction.block();
    }

}
