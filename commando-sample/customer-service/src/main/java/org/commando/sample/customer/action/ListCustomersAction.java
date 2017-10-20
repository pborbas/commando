package org.commando.sample.customer.action;

import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.ListCustomersCommand;
import org.commando.sample.customer.api.command.ListCustomersResult;
import org.commando.sample.customer.repo.CustomerRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

@DispatchAction
public class ListCustomersAction extends AbstractAction<ListCustomersCommand, ListCustomersResult>{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public ListCustomersResult execute(final ListCustomersCommand listCustomersCommand) throws DispatchException {
        return new ListCustomersResult(listCustomersCommand.getCommandId(), this.customerRepository.findAll());
    }

}
