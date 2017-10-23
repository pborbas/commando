package org.commando.sample.product;

import org.apache.activemq.command.ActiveMQQueue;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.jms.receiver.JmsCommandReceiverListener;
import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.product.dispatcher.ProductDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

/**
 *
 */
@Configuration
public class CommandoJmsReceiverConfiguration {

	@Bean
	public Serializer serializer() {
		return new XmlSerializer();
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
