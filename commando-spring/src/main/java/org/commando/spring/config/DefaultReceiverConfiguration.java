package org.commando.spring.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.commando.dispatcher.ChainableDispatcher;
import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.spring.dispatcher.SpringInVmDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultReceiverConfiguration {

    @Bean
    public Serializer serializer() {
        return new XmlSerializer();
    }

    @Bean
    public ExecutorService executor() {
        return new ThreadPoolExecutor(8, 50, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

    @Bean
    public ChainableDispatcher dispatcher() {
        SpringInVmDispatcher dispatcher = new SpringInVmDispatcher();
        dispatcher.setExecutorService(executor());
        return dispatcher;
    }

    @Bean
    public CommandReceiver commandReceiver() {
        return new DefaultCommandReceiver(serializer(), dispatcher());
    }
}
