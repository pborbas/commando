package org.commando.sample.gw.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class CustomerDispatcherConfiguration {
private static final Log LOG = LogFactory.getLog(CustomerDispatcherConfiguration.class);

	@Bean
	public Serializer customerSerializer() {
		return new XmlSerializer();
	}

	@Bean
	public CustomerDispatcher customerDispatcher() {
		CustomerHttpDispatcher dispatcher = new CustomerHttpDispatcher("http://localhost:8881/api/customer",
				customerSerializer());
		return dispatcher;
	}

}
