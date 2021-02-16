package org.commando.remote.http.exception;

import org.commando.exception.DispatchException;

/**
 * An http dispatcher can use this to transform it to proper status codes
 */
public class UnauthenticatedException extends DispatchException implements ExceptionWithHttpStatus {

	public UnauthenticatedException(String message) {
		super(message);
	}

	public UnauthenticatedException(String message, Throwable e) {
		super(message, e);
	}

	@Override
	public int getHttpStatusCode() {
		return 401;
	}

}
