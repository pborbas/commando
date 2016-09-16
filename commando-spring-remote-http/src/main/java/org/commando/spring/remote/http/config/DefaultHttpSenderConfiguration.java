package org.commando.spring.remote.http.config;

import org.commando.remote.http.dispatcher.RestHttpDispatcherFactory;
import org.commando.remote.serializer.Serializer;
import org.commando.spring.core.dispatcher.SpringDispatcherFactory;
import org.commando.spring.core.dispatcher.serializer.SpringSerializerFactory;
import org.commando.xml.serializer.XmlSerializer;
import org.commando.xml.serializer.XmlSerializerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Deprecated //no need for this.
@Configuration
public class DefaultHttpSenderConfiguration {

    @Bean
    public Serializer serializer() {
        return new XmlSerializer();
    }

    @Bean
    public XmlSerializerFactory xmlSerializerFactory() {
        return new XmlSerializerFactory();
    }

    @Bean
    public SpringSerializerFactory springSerializerFactory() {
        return new SpringSerializerFactory();
    }

    @Bean
    public RestHttpDispatcherFactory restHttpDispatcherFactory() {
        return new RestHttpDispatcherFactory(springSerializerFactory());
    }

//TODO: organize it into the JMS project or create an abstract configuration with a registerDispatcherFactories method
//    @Bean
//    public JmsDispatcherFactory JmsDispatcherFactory() {
//        return new JmsDispatcherFactory(springSerializerFactory());
//    }

    @Bean
    public SpringDispatcherFactory springDispatcherFactory() {
        return new SpringDispatcherFactory();
    }

    @Bean
    public ExecutorService executor() {
        return new ThreadPoolExecutor(8, 50, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

}
