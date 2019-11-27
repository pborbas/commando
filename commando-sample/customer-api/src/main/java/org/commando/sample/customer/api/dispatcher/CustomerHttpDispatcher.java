package org.commando.sample.customer.api.dispatcher;

import org.commando.remote.http.dispatcher.HttpDispatcher;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class CustomerHttpDispatcher extends HttpDispatcher implements CustomerDispatcher{
	public CustomerHttpDispatcher(String targetUrl, Serializer serializer) {
		super(targetUrl, serializer);
	}
}
