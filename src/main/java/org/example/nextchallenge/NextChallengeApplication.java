package org.example.nextchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.nextchallenge")
public class NextChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NextChallengeApplication.class, args);
    }

}
