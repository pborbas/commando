package org.commando.spring.boot;

import org.commando.spring.config.DefaultReceiverConfiguration;
import org.commando.spring.remote.http.receiver.SpringCommandReceiverSerlvet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnableHttpCommandReceiver extends DefaultReceiverConfiguration {

    @Bean
    public ServletRegistrationBean commandDispatcherServletRegistration() {
	return new ServletRegistrationBean(new SpringCommandReceiverSerlvet(), "/dispatcher/*");
    }

}
