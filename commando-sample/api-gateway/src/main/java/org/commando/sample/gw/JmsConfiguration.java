package org.commando.sample.gw;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Configures the dispatchers of the module
 */
@Configuration
public class JmsConfiguration {

	@Bean
	public ConnectionFactory jmsConnectionFactory() {
		return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
	}


}
