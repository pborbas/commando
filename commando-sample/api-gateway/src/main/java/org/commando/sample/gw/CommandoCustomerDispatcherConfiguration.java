package org.commando.sample.gw;

import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.dispatcher.CustomerHttpDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the dispatchers of the module
 */
@Configuration
public class CommandoCustomerDispatcherConfiguration {

	@Bean
	public Serializer customerSerializer() {
		return new XmlSerializer();
	}

	@Bean
	public CustomerDispatcher customerDispatcher() {
		return new CustomerHttpDispatcher("http://localhost:8881/api/customer", customerSerializer());
	}

}
