package org.commando.remote.http.receiver;

import org.commando.dispatcher.ChainableDispatcher;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class DefaultHttpCommandReceiver extends DefaultCommandReceiver implements HttpCommandReceiver{

	private final String mappingUrl;
	private final String servletName;

	public DefaultHttpCommandReceiver(Serializer serializer, ChainableDispatcher dispatcher, String mappingUrl) {
		this(serializer, dispatcher, mappingUrl, dispatcher.getClass().getSimpleName());
	}

	public DefaultHttpCommandReceiver(Serializer serializer, ChainableDispatcher dispatcher, String mappingUrl,
			String servletName) {
		super(serializer, dispatcher);
		this.mappingUrl = mappingUrl;
		this.servletName = servletName;
	}

	@Override
	public String getMappingUrl() {
		return mappingUrl;
	}

	@Override
	public String getServletName() {
		return this.servletName;
	}

}
