package org.commando.sample.customer.action;

import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.result.LongResult;
import org.commando.sample.customer.api.CreateCustomerCommand;
import org.commando.sample.customer.repo.CustomerRepository;
import org.commando.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DispatchAction
public class CreateCustomerAction extends AbstractAction<CreateCustomerCommand, LongResult>{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional
    public LongResult execute(final CreateCustomerCommand createCustomerCommand) throws DispatchException {
        long customerId=this.customerRepository.save(createCustomerCommand.getCustomer()).getId();
        return new LongResult(createCustomerCommand.getCommandId(), customerId);
    }

}
