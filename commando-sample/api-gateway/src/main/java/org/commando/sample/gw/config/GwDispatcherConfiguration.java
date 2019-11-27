package org.commando.sample.gw.config;

import org.commando.sample.gw.dispatcher.GwDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configures the dispatchers of the module
 */
@Configuration
public class GwDispatcherConfiguration {

	@Bean
	public GwDispatcher gwDispatcher() {
		GwDispatcher gwDispatcher = new GwDispatcher();
		gwDispatcher.setExecutorService(Executors.newFixedThreadPool(20));
		return gwDispatcher;
	}

}
