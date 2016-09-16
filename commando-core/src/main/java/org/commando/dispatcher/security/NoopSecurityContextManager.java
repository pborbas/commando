package org.commando.dispatcher.security;

/**
 * Created by pborbas on 22/03/16.
 */
public class NoopSecurityContextManager implements SecurityContextManager<Object> {
	@Override
	public Object getSecurityContext() {
		return null;
	}

	@Override
	public void setSecurityContext(Object securityContextInformation) {

	}

	@Override
	public void clearSecurityContext() {

	}
}
