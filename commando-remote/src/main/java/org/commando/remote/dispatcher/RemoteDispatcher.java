package org.commando.remote.dispatcher;

import org.commando.dispatcher.Dispatcher;

/**
 * Executes actions remotely and returns the results.
 * 
 */
public interface RemoteDispatcher extends Dispatcher {
    public static final String HEADER_RESULT_EXCEPTION_CLASS="resultExceptionClass";

}
