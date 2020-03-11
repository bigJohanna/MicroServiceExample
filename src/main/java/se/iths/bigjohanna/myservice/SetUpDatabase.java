package se.iths.bigjohanna.myservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class SetUpDatabase {

    @Bean
    CommandLineRunner fillDatabase(UsersRepository repository){
        return args -> {
            repository.save(new User(0,"kallis", "Anna Karlhagen", "Stockholm", 25000f, false));
            repository.save(new User(0,"moalund", "Moa Lund", "Helsingborg", 60000f, true));
            repository.save(new User(0,"karlstad45", "Nellie Spade", "Karlstad", 40000f, false));
        };
    }

    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
