package org.tokenator.opentokenizer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@ComponentScan
@EnableAutoConfiguration
//@EntityScan("org.tokenator.opentokenizer.domain.entity")
//@EnableJpaRepositories("org.tokenator.opentokenizer.domain.repository")
public class Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Application.class, args);
    }
}
