package org.commando.spring.core.dispatcher;

import java.util.List;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.DispatcherFactory;
import org.commando.remote.model.DispatcherUrl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Holds all dispatcher factories in the spring context (@Autowired)
 * 
 * @author pborbas
 */
public class SpringDispatcherFactory implements DispatcherFactory {

    List<DispatcherFactory> factories;


    private DispatcherFactory findFactory(final DispatcherUrl url) {
        for (DispatcherFactory factory : factories) {
            if (factory.isFactoryFor(url)) {
                return factory;
            }
        }
        return null;
    }

    @Override
    public Dispatcher create(final DispatcherUrl url) throws DispatchException {
        DispatcherFactory factory = findFactory(url);
        if (factory != null) {
            return factory.create(url);
        }
        throw new DispatchException("Cannot find dispatcher factory for URL:" + url);
    }

    @Override
    public boolean isFactoryFor(final DispatcherUrl url) {
        return findFactory(url) != null;
    }

    @Autowired
    public void setFactories(final List<DispatcherFactory> factories) {
        this.factories = factories;
    }

    public List<DispatcherFactory> getFactories() {
        return factories;
    }
}
