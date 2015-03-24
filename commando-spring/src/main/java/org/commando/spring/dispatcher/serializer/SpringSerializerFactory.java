package org.commando.spring.dispatcher.serializer;

import java.util.List;

import org.commando.remote.serializer.Serializer;
import org.commando.remote.serializer.SerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringSerializerFactory implements SerializerFactory {

    @Autowired
    List<SerializerFactory> factories;

    public List<SerializerFactory> getFactories() {
        return factories;
    }

    @Override
    public Serializer create(final String serializerName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isFactoryFor(final String serializerName) {
        // TODO Auto-generated method stub
        return false;
    }
}
