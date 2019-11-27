package org.commando.sample.gw.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.GetCustomerCommand;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	private static final Log LOG = LogFactory.getLog(CustomerController.class);
	private final CustomerDispatcher customerDispatcher;

	@Autowired
	public CustomerController(CustomerDispatcher customerDispatcher) {
		this.customerDispatcher = customerDispatcher;
	}

	@RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
	public Mono<Customer> getCustomers(@PathVariable Long customerId) throws DispatchException {
//		Mono<Customer> customerMono = Mono.fromFuture(this.customerDispatcher.dispatch(new GetCustomerCommand(customerId)))
//				.map(customerResult -> customerResult.getValue());
		return this.customerDispatcher.dispatch(new GetCustomerCommand(customerId)).map(customerResult -> customerResult.getValue());
	}

//	@RequestMapping(method = RequestMethod.GET)
//	public Mono<Customer> listCustomers() throws DispatchException {
//		return this.customerDispatcher.dispatchSync(new ListCustomersCommand()).getCustomers();
//	}
//
//	@RequestMapping(method = RequestMethod.POST)
//	public Customer createCustomer(@RequestParam final String name) throws DispatchException {
//		return this.customerDispatcher.dispatchSync(new CreateCustomerCommand(name)).getValue();
//	}

}
