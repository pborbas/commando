package org.commando.sample.customer;

import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.dispatcher.CustomerInVmDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the dispatcher of the module
 */
@Configuration
public class CommandoDispatcherConfiguration {

	@Bean
	public CustomerDispatcher customerDispatcher() {
		return new CustomerInVmDispatcher();
	}

}
