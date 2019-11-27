package org.commando.remote.http.dispatcher;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.DispatcherFactory;
import org.commando.remote.model.DispatcherUrl;
import org.commando.remote.serializer.SerializerFactory;

public class HttpDispatcherFactory implements DispatcherFactory {

    private final SerializerFactory serializerFactory;

    public HttpDispatcherFactory(final SerializerFactory serializerFactory) {
        super();
        this.serializerFactory = serializerFactory;
    }

    @Override
    public Dispatcher create(final DispatcherUrl url) throws DispatchException {
        if (isFactoryFor(url)) {
            HttpDispatcher dispatcher = new HttpDispatcher(createTargetUrl(url), serializerFactory.create(url.getSerializer()));
            dispatcher.setTimeout(url.getTimeout());
            return dispatcher;
        }
        throw new DispatchException("Cannot create HTTP dispatcher for url:" + url);
    }

    protected String createTargetUrl(final DispatcherUrl url) {
        String targetUrl = url.getProtocol() + "://" + url.getHostAndPort();
        if (url.getPath() != null && url.getPath().length() > 0) {
            targetUrl += "/" + url.getPath();
        }
        return targetUrl;
    }

    @Override
    public boolean isFactoryFor(final DispatcherUrl url) {
        return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }
}
