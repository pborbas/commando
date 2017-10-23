package org.commando.sample.product;

import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.commando.sample.product.dispatcher.ProductInVmDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the dispatcher of the module
 */
@Configuration
public class CommandoDispatcherConfiguration {

	@Bean
	public ProductDispatcher customerDispatcher() {
		return new ProductInVmDispatcher();
	}

}
