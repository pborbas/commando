package org.commando.remote.dispatcher.filter.circuit;

import org.commando.remote.exception.RemoteDispatchException;

/**
 * Thrown when circuit breaker refuses the call
 * 
 * @author pborbas
 *
 */
public class CircuiteBreakerException extends RemoteDispatchException {

    private static final long serialVersionUID = 1L;

    public CircuiteBreakerException(final String message) {
        super(message);
    }

    public CircuiteBreakerException(final String message, final Exception e) {
        super(message, e);
    }

}
