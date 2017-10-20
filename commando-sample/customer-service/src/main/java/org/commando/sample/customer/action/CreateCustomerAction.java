package org.commando.sample.customer.action;

import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CreateCustomerCommand;
import org.commando.sample.customer.api.command.CustomerResult;
import org.commando.sample.customer.api.model.Customer;
import org.commando.sample.customer.repo.CustomerRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DispatchAction
public class CreateCustomerAction extends AbstractAction<CreateCustomerCommand, CustomerResult>{


    private final CustomerRepository customerRepository;

    @Autowired
	public CreateCustomerAction(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
    @Transactional
    public CustomerResult execute(final CreateCustomerCommand createCustomerCommand) throws DispatchException {
        Customer customer=this.customerRepository.save(new Customer(createCustomerCommand.getName()));
        return new CustomerResult(createCustomerCommand.getCommandId(), customer);
    }

}
