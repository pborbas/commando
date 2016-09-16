package org.commando.dispatcher.filter.retry;

import org.commando.exception.DispatchException;

/**
 * Created by pborbas on 03/05/16.
 */
//TODO: commando: move it to Commando

public class LimitedRetryException extends DispatchException {
	public LimitedRetryException(String message) {
		super(message);
	}
}
