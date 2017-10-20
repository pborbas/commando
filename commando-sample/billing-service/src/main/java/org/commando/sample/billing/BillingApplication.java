package org.commando.sample.billing;

import org.commando.spring.boot.remote.jms.config.EnableJmsCommandDefaultReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
@Import(EnableJmsCommandDefaultReceiver.class)
public class BillingApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }
}
