package org.commando.sample.gw.controller;

import org.commando.exception.DispatchException;
import org.commando.sample.customer.api.command.CreateCustomerCommand;
import org.commando.sample.customer.api.command.ListCustomersCommand;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerDispatcher customerDispatcher;

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> listCustomers() throws DispatchException {
        return this.customerDispatcher.dispatchSync(new ListCustomersCommand()).getCustomers();
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public Customer createCustomer(@PathVariable final long id, @RequestParam final String name) throws DispatchException {
        return this.customerDispatcher.dispatchSync(new CreateCustomerCommand(name)).getValue();
    }


}
