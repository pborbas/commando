package org.commando.xml.serializer;

import org.commando.remote.serializer.Serializer;
import org.commando.remote.serializer.SerializerFactory;

public class XmlSerializerFactory implements SerializerFactory {
    @Override
    public boolean isFactoryFor(String serializerName) {
	return "xml".equals(serializerName);
    }

    @Override
    public Serializer create(String serializerName) {
	return new XmlSerializer();
    }
}
