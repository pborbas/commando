package org.commando.sample.gw.controller;

import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CreateCustomerCommand;
import org.commando.sample.customer.api.command.ListCustomersCommand;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerDispatcher customerDispatcher;

    @Autowired
	public CustomerController(CustomerDispatcher customerDispatcher) {
		this.customerDispatcher = customerDispatcher;
	}

	@RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() throws DispatchException {
        return this.customerDispatcher.dispatchSync(new ListCustomersCommand()).getCustomers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Customer createCustomer(@RequestParam final String name) throws DispatchException {
        return this.customerDispatcher.dispatchSync(new CreateCustomerCommand(name)).getValue();
    }


}
