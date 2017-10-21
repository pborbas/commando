package org.commando.spring.boot.remote.jms.config;

import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.jms.receiver.JmsCommandReceiverListener;
import org.commando.spring.remote.config.AbstractDefaultReceiverConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

public abstract class AbstractJmsCommandReceiverConfiguration extends AbstractDefaultReceiverConfiguration {

	public abstract Destination commandDestination();
	public abstract Destination resultDestination();

	public JmsTemplate resultJmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory, resultDestination());
	}

	public JmsCommandReceiverListener jmsCommandReceiverListener(ConnectionFactory connectionFactory) {
		return new JmsCommandReceiverListener(commandReceiver(), resultJmsTemplate(connectionFactory));
	}

	@Bean
	public DefaultMessageListenerContainer commandMessageListenerContainer(ConnectionFactory connectionFactory) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setMessageListener(jmsCommandReceiverListener(connectionFactory));
		container.setDestination(commandDestination());
		container.setConnectionFactory(connectionFactory);
		return container;
	}

}
