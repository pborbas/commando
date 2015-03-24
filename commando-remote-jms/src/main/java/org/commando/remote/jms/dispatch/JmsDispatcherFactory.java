package org.commando.remote.jms.dispatch;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.DispatcherFactory;
import org.commando.remote.model.DispatcherUrl;
import org.commando.remote.serializer.SerializerFactory;

public class JmsDispatcherFactory implements DispatcherFactory {

    private final SerializerFactory serializerFactory;

    public JmsDispatcherFactory(final SerializerFactory serializerFactory) {
        super();
        this.serializerFactory = serializerFactory;
    }

    @Override
    public Dispatcher create(final DispatcherUrl url) throws DispatchException {
        if (isFactoryFor(url)) {
            ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(url.getHostAndPort());
            Destination commandDestination = new ActiveMQQueue(url.getParameters().get("commandQueue"));
            JmsTemplate commandJmsTemplate = new JmsTemplate(connectionFactory, commandDestination);
            Destination resultDestination = new ActiveMQQueue(url.getParameters().get("resultQueue"));
            JmsTemplate resultJmsTemplate = new JmsTemplate(connectionFactory, resultDestination);
            return new JmsDispatcher(commandJmsTemplate, resultJmsTemplate, serializerFactory.create(url.getSerializer()));
        }
        throw new DispatchException("Cannot create JMS dispatcher for url:" + url);
    }

    protected String createTargetUrl(final DispatcherUrl url) {
        return url.getProtocol() + "://" + url.getHostAndPort() + "/" + url.getPath();
    }

    @Override
    public boolean isFactoryFor(final DispatcherUrl url) {
        return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }
}
