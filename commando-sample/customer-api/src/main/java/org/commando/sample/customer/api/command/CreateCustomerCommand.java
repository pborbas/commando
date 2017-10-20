package org.commando.sample.customer.api.command;

import org.commando.command.AbstractCommand;

public class CreateCustomerCommand extends AbstractCommand<CustomerResult>{

    private final String name;

	public CreateCustomerCommand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
