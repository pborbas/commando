package org.commando.exception;
/**
 * Thrown in case of async execution throws exception
 * @author pborbas
 *
 */
public class AsyncExecutionException extends DispatchException {
    private static final long serialVersionUID = 1L;

	public AsyncExecutionException(String message) {
		super(message);
	}

	public AsyncExecutionException(final String message, final Throwable e) {
        super(message, e);
    }

}
