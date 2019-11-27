package org.commando.sample.customer.config;

import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.dispatcher.CustomerInVmDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configures the dispatcher and receiver
 */
@Configuration
public class CommandoConfiguration {

	@Bean
	public CustomerInVmDispatcher customerDispatcher() {
		CustomerInVmDispatcher dispatcher = new CustomerInVmDispatcher();
		dispatcher.setExecutorService(Executors.newFixedThreadPool(250));
		return dispatcher;

	}

	@Bean
	public Serializer serializer() {
		return new XmlSerializer();
	}

	@Bean
	public CommandReceiver customerCommandReceiver() {
		return new DefaultCommandReceiver(serializer(), customerDispatcher());
	}

}
