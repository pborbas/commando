package org.commando.exception;
/**
 * Thrown in case of async timeout
 *
 * @author pborbas
 *
 */
public class AsyncTimeoutException extends AsyncExecutionException {
    private static final long serialVersionUID = 1L;

	public AsyncTimeoutException(String message) {
		super(message);
	}

	public AsyncTimeoutException(final String message, final Exception e) {
        super(message, e);
    }

}
