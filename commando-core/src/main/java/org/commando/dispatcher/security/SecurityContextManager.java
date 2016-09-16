package org.commando.dispatcher.security;

/**
 * Created by pborbas on 22/03/16.
 * General interface to let the dispatcher get the security context form the executor thread, delegate it to the async thread
 * and finally clear it on the async thread.
 * Used to delegate frameworks security context to async threads
 */
public interface SecurityContextManager<T> {
	T getSecurityContext();
	void setSecurityContext(T securityContextInformation);
	void clearSecurityContext();
}
