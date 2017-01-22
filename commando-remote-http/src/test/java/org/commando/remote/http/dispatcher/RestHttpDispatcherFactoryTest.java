package org.commando.remote.http.dispatcher;

import org.commando.dispatcher.Dispatcher;
import org.commando.example.SampleCommand;
import org.commando.exception.DispatchException;
import org.commando.json.serializer.JsonSerializerFactory;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.model.DispatcherUrl;
import org.commando.remote.serializer.SerializerFactory;
import org.junit.Assert;
import org.junit.Test;

public class RestHttpDispatcherFactoryTest {

    @Test
    public void testCreateDispatcherByUrl() throws DispatchException {
        SerializerFactory serializerFactory=new JsonSerializerFactory();
        RestHttpDispatcherFactory dispatcherFactory=new RestHttpDispatcherFactory(serializerFactory);
        DispatcherUrl dispatcherUrl=DispatcherUrl.valueOf("http://restdispatcher:xml@127.0.0.1:9999/dispatcher?timeout=5000");
        Assert.assertTrue(dispatcherFactory.isFactoryFor(dispatcherUrl));
        Dispatcher dispatcher=dispatcherFactory.create(dispatcherUrl);
        Assert.assertTrue(dispatcher instanceof RestHttpDispatcher);
        Assert.assertEquals(5000, ((RestHttpDispatcher)dispatcher).getTimeout());
        try {
            dispatcher.dispatch(new SampleCommand()).getResult();
        } catch (RemoteDispatchException e) {
            Assert.assertTrue(e.getMessage().contains("http://127.0.0.1:9999"));
        }
    }
}
