package org.commando.sample.customer.api.command;

import org.commando.command.AbstractCommand;

/**
 *
 */
public class GetCustomerCommand extends AbstractCommand<CustomerResult> {
	private final Long customerId;

	public GetCustomerCommand(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCustomerId() {
		return customerId;
	}
}
