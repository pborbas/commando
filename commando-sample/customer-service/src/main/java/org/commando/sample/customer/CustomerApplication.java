package org.commando.sample.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CustomerApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
