package org.commando.remote.http.exception;

import org.commando.exception.DispatchException;

/**
 * An http dispatcher can use this to transform it to proper status codes/
 */
public class NotFoundException extends DispatchException implements ExceptionWithHttpStatus {

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable e) {
		super(message, e);
	}

	@Override
	public int getHttpStatusCode() {
		return 404;
	}

}
