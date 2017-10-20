package org.commando.sample.customer.api.command;

import org.commando.result.AbstractSimpleResult;
import org.commando.sample.customer.api.model.Customer;

/**
 *
 */
public class CustomerResult extends AbstractSimpleResult<Customer> {
	public CustomerResult(String commandId, Customer value) {
		super(commandId, value);
	}
}
