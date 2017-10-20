package org.commando.sample.customer;

import org.commando.remote.http.receiver.DefaultHttpCommandReceiver;
import org.commando.remote.http.receiver.HttpCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.dispatcher.CustomerInVmDispatcher;
import org.commando.spring.remote.http.config.AbstractHttpCommandDefaultReceiverConfiguration;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class CommandoConfiguration extends AbstractHttpCommandDefaultReceiverConfiguration {

	@Bean
	public Serializer serializer() {
		return new XmlSerializer();
	}

	@Bean
	public CustomerDispatcher customerDispatcher() {
		return new CustomerInVmDispatcher();
	}

	@Bean
	public HttpCommandReceiver customerHttpCommandReceiver() {
		return new DefaultHttpCommandReceiver(serializer(), customerDispatcher(), "/api/customer", "customerServlet");
	}

}
