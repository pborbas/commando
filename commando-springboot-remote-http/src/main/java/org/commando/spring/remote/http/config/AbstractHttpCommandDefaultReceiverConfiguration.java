package org.commando.spring.remote.http.config;

import org.commando.remote.http.receiver.HttpCommandReceiver;
import org.commando.spring.remote.config.AbstractDefaultReceiverConfiguration;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletContext;
import java.util.List;

/**
 *
 */
public class AbstractHttpCommandDefaultReceiverConfiguration extends AbstractDefaultReceiverConfiguration {

	@Bean
	public HttpCommandReceiverServletRegistration httpCommandReceiverServletRegistration(
			List<HttpCommandReceiver> httpCommandReceivers, ServletContext servletContext) {
		return new HttpCommandReceiverServletRegistration(httpCommandReceivers);
	}
}
