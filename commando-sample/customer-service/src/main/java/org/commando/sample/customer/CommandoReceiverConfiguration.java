package org.commando.sample.customer;

import org.commando.remote.http.receiver.DefaultHttpCommandReceiver;
import org.commando.remote.http.receiver.HttpCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.spring.remote.http.config.AbstractHttpCommandDefaultReceiverConfiguration;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers a receiver servlet that accepts commands through HTTP on the specified base URL and delegates them to the Dispatcher configured in @{@link CommandoDispatcherConfiguration}
 */
@Configuration
public class CommandoReceiverConfiguration extends AbstractHttpCommandDefaultReceiverConfiguration {

	@Bean
	public Serializer serializer() {
		return new XmlSerializer();
	}

	@Bean
	public HttpCommandReceiver customerHttpCommandReceiver(CustomerDispatcher customerDispatcher) {
		return new DefaultHttpCommandReceiver(serializer(), customerDispatcher, "/api/customer/*", "customerServlet");
	}

}
