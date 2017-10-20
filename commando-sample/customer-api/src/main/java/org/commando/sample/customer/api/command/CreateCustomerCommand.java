package org.commando.sample.customer.api.command;

import org.commando.command.AbstractCommand;
import org.commando.result.LongResult;
import org.commando.sample.customer.api.model.Customer;

public class CreateCustomerCommand extends AbstractCommand<LongResult>{

    private final Customer customer;

    public CreateCustomerCommand(final Customer customer) {
        this.customer=customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
