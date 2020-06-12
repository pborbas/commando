package org.commando.sample.customer.action;

import org.commando.core.reactive.action.ReactiveAction;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CustomerResult;
import org.commando.sample.customer.api.command.GetCustomerCommand;
import org.commando.sample.customer.api.exception.CustomerNotFoundException;
import org.commando.sample.customer.api.model.Customer;
import org.commando.sample.customer.repo.CustomerRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *
 */
@DispatchAction
public class GetCustomerAction implements ReactiveAction<GetCustomerCommand, CustomerResult> {

	private final CustomerRepository customerRepository;

	@Autowired
	public GetCustomerAction(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Mono<CustomerResult> execute(GetCustomerCommand command) throws DispatchException {
		if (command.getCustomerId()>100) {
			throw new CustomerNotFoundException("Customer with id: "+command.getCustomerId()+" not found. Use id<100 for success results");
		}
		Customer customer = new Customer(command.getCustomerId(), "John Smith");
		Mono<CustomerResult> resultMono=Mono.just(new CustomerResult(command.getCommandId(), customer));
		return resultMono.delayElement(Duration.ofMillis(100));
	}

	@Override
	public Class<GetCustomerCommand> getCommandType() {
		return GetCustomerCommand.class;
	}
}
