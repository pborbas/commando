package org.commando.exception;

public class ActionNotFoundException extends DispatchException {
	private static final long serialVersionUID = 1L;

	public ActionNotFoundException(final String message) {
		super(message);
	}

}
