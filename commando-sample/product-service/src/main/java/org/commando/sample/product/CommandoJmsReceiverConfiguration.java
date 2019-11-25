package org.commando.sample.product;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.command.ActiveMQQueue;
import org.commando.json.serializer.JsonSerializer;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.jms.receiver.JmsCommandReceiverListener;
import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.net.URI;

/**
 *
 */
@Configuration
public class CommandoJmsReceiverConfiguration {

	public static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

	@Bean
	public BrokerService brokerService() throws Exception {
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI(DEFAULT_BROKER_URL));
		broker.addConnector(connector);
		return broker;
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
		return connectionFactory;
	}

	@Bean
	public Serializer serializer() {
		return new JsonSerializer();
	}

	@Bean
	public Destination commandDestination() {
		return new ActiveMQQueue("COMMAND.QUEUE");
	}

	@Bean
	public Destination resultDestination() {
		return new ActiveMQQueue("RESULT.QUEUE");
	}

	@Bean
	public CommandReceiver commandReceiver(ProductDispatcher productDispatcher) {
		return new DefaultCommandReceiver(serializer(), productDispatcher);
	}

	@Bean
	public JmsTemplate resultJmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory, resultDestination());
	}

	@Bean
	public JmsTemplate commandJmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory, commandDestination());
	}

	@Bean
	public JmsCommandReceiverListener jmsCommandReceiverListener(ConnectionFactory connectionFactory, ProductDispatcher productDispatcher) {
		return new JmsCommandReceiverListener(commandReceiver(productDispatcher), resultJmsTemplate(connectionFactory));
	}

	@Bean
	public DefaultMessageListenerContainer commandMessageListenerContainer(ConnectionFactory connectionFactory, ProductDispatcher productDispatcher) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setMessageListener(jmsCommandReceiverListener(connectionFactory, productDispatcher));
		container.setDestination(commandDestination());
		container.setConnectionFactory(connectionFactory);
		return container;
	}

}
