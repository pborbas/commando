package org.commando.sample.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ProductApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
}
