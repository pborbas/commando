package org.commando.remote.serializer;

public interface SerializerFactory {
    boolean isFactoryFor(String serializerName);

    Serializer create(String serializerName);
}
