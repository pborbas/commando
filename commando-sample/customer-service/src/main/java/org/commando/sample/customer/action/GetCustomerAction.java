package org.commando.sample.customer.action;

import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CustomerResult;
import org.commando.sample.customer.api.command.GetCustomerCommand;
import org.commando.sample.customer.repo.CustomerRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@DispatchAction
public class GetCustomerAction extends AbstractAction<GetCustomerCommand, CustomerResult> {

	private final CustomerRepository customerRepository;

	@Autowired
	public GetCustomerAction(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public CustomerResult execute(GetCustomerCommand command) throws DispatchException {
		return new CustomerResult(command.getCommandId(), this.customerRepository.findOne(command.getCustomerId()));
	}
}
