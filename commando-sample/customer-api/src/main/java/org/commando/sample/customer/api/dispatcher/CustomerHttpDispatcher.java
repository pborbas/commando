package org.commando.sample.customer.api.dispatcher;

import org.commando.remote.http.dispatcher.ReactiveHttpDispatcher;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class CustomerHttpDispatcher extends ReactiveHttpDispatcher implements CustomerDispatcher{
	public CustomerHttpDispatcher(String targetUrl, Serializer serializer) {
		super(targetUrl, serializer);
//		setExecutorService(Executors.newFixedThreadPool(250));
	}
}
//public class CustomerHttpDispatcher extends HttpDispatcher implements CustomerDispatcher{
//	public CustomerHttpDispatcher(String targetUrl, Serializer serializer) {
//		super(targetUrl, serializer, 250);
//	}
//}
