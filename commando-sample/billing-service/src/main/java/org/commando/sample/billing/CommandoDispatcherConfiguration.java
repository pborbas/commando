package org.commando.sample.billing;

import org.commando.sample.billing.dispatcher.BillingDispatcher;
import org.commando.sample.billing.dispatcher.BillingInVmDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the dispatcher of the module
 */
@Configuration
public class CommandoDispatcherConfiguration {

	@Bean
	public BillingDispatcher customerDispatcher() {
		return new BillingInVmDispatcher();
	}

}
