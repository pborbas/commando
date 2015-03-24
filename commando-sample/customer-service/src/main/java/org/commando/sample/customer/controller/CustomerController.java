package org.commando.sample.customer.controller;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.result.LongResult;
import org.commando.sample.customer.api.CreateCustomerCommand;
import org.commando.sample.customer.api.ListCustomersCommand;
import org.commando.sample.customer.api.ListCustomersResult;
import org.commando.sample.customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private Dispatcher dispatcher;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String dispatcherInfo() throws DispatchException {
        //TODO: this info could be available by default with spring boot
        return this.dispatcher.toString().replaceAll("\n", "<BR/>");
    }


    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ListCustomersResult listCustomers() throws DispatchException {
        return this.dispatcher.dispatch(new ListCustomersCommand()).getResult();
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET) 
    public LongResult createCustomer(@PathVariable final long id, @RequestParam final String name) throws DispatchException {
        Customer customer=new Customer(id, name);
        return this.dispatcher.dispatch(new CreateCustomerCommand(customer)).getResult();
    }


}
