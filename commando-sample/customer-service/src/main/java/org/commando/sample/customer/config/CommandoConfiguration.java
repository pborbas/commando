package org.commando.sample.customer.config;

import org.commando.core.reactive.action.ReactiveAction;
import org.commando.exception.DuplicateActionException;
import org.commando.remote.http.receiver.DefaultReactiveCommandReceiver;
import org.commando.remote.http.receiver.ReactiveCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.dispatcher.CustomerInVmDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configures the dispatcher and receiver
 */
@Configuration
public class CommandoConfiguration {

	@Bean
	public CustomerInVmDispatcher customerDispatcher(List<ReactiveAction> actions) throws DuplicateActionException {
		CustomerInVmDispatcher dispatcher = new CustomerInVmDispatcher();
		dispatcher.setActions(actions);
//		dispatcher.setExecutorService(Executors.newFixedThreadPool(250));
		return dispatcher;

	}

	@Bean
	public Serializer serializer() {
		return new XmlSerializer();
	}

	@Bean
	public ReactiveCommandReceiver customerCommandReceiver(CustomerInVmDispatcher dispatcher) {
		return new DefaultReactiveCommandReceiver(serializer(), dispatcher);
	}


}
