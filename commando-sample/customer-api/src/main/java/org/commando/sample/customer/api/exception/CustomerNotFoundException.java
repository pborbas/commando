package org.commando.sample.customer.api.exception;

import org.commando.remote.http.exception.NotFoundException;

/**
 *
 */
public class CustomerNotFoundException extends NotFoundException {
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
