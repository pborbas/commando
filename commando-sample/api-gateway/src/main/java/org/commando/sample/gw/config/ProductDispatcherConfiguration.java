package org.commando.sample.gw.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.commando.json.serializer.JsonSerializer;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.product.dispatcher.ProductJmsDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

/**
 * Configures the dispatcher of the module
 */
@Configuration
public class ProductDispatcherConfiguration {

	public static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
		return connectionFactory;
	}

	@Bean
	public Serializer productSerializer() {
		return new JsonSerializer();
	}

	@Bean
	public ProductJmsDispatcher productDispatcher(ConnectionFactory connectionFactory) {
		ActiveMQQueue commandQueue = new ActiveMQQueue("COMMAND.QUEUE");
		JmsTemplate commandJmsTemplate = new JmsTemplate(connectionFactory, commandQueue);
		commandJmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		ActiveMQQueue resultQueue = new ActiveMQQueue("RESULT.QUEUE");
		JmsTemplate resultJmsTemplate = new JmsTemplate(connectionFactory, resultQueue);
		return new ProductJmsDispatcher(commandJmsTemplate, resultJmsTemplate, productSerializer());
	}

}
