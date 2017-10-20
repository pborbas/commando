package org.commando.sample.customer.api.dispatcher;

import org.commando.remote.http.dispatcher.RestHttpDispatcher;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class CustomerHttpDispatcher extends RestHttpDispatcher implements CustomerDispatcher{
	public CustomerHttpDispatcher(String targetUrl, Serializer serializer) {
		super(targetUrl, serializer);
	}
}
