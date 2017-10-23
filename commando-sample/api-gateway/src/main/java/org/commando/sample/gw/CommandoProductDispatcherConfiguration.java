package org.commando.sample.gw;

import org.apache.activemq.command.ActiveMQQueue;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.product.dispatcher.ProductJmsDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Configures the dispatcher of the module
 */
@Configuration
public class CommandoProductDispatcherConfiguration {

	@Bean
	public Serializer productSerializer() {
		return new XmlSerializer();
	}

	@Bean
	public ProductJmsDispatcher productDispatcher(ConnectionFactory connectionFactory) {
		ActiveMQQueue commandQueue = new ActiveMQQueue("COMMAND.QUEUE");
		JmsTemplate commandJmsTemplate = new JmsTemplate(connectionFactory, commandQueue);
		ActiveMQQueue resultQueue = new ActiveMQQueue("RESULT.QUEUE");
		JmsTemplate resultJmsTemplate = new JmsTemplate(connectionFactory, resultQueue);
		return new ProductJmsDispatcher(commandJmsTemplate, resultJmsTemplate, productSerializer());
	}

}