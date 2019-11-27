package org.commando.spring.remote.config;

import org.commando.dispatcher.Dispatcher;
import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.spring.core.dispatcher.SpringInVmDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Extend it as a @Configuration class to have a default receiver for all your actions
 */
@Deprecated
public abstract class AbstractDefaultReceiverConfiguration {

    @Bean
    public Serializer serializer() {
        return new XmlSerializer();
    }

    @Bean
    public ExecutorService executor() {
        return new ThreadPoolExecutor(8, 50, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
    }

    @Bean
    public Dispatcher dispatcher() {
        SpringInVmDispatcher dispatcher = new SpringInVmDispatcher();
        dispatcher.setExecutorService(executor());
        return dispatcher;
    }

    @Bean
    public CommandReceiver commandReceiver() {
        return new DefaultCommandReceiver(serializer(), dispatcher());
    }
}
