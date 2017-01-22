package org.commando.json.serializer;

import org.commando.remote.serializer.Serializer;
import org.commando.remote.serializer.SerializerFactory;

public class JsonSerializerFactory implements SerializerFactory {
    @Override
    public boolean isFactoryFor(String serializerName) {
	return "json".equals(serializerName);
    }

    @Override
    public Serializer create(String serializerName) {
	return new JsonSerializer();
    }
}
