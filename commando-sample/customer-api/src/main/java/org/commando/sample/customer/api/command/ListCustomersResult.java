package org.commando.sample.customer.api.command;

import org.commando.result.AbstractResult;
import org.commando.sample.customer.api.model.Customer;

import java.util.List;

public class ListCustomersResult extends AbstractResult {

    private final List<Customer> customers;

    public ListCustomersResult(final String commandId, final List<Customer> customers) {
        super(commandId);
        this.customers=customers;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

}
