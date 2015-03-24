package org.commando.sample.billing.controller;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.sample.billing.api.CreateInvoiceCommand;
import org.commando.sample.billing.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {

    @Autowired
    private Dispatcher dispatcher;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String dispatcherInfo() throws DispatchException {
        return this.dispatcher.toString().replaceAll("\n", "<BR/>");
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public void createInvoice(@RequestBody final Invoice invoice) throws DispatchException {
        this.dispatcher.dispatch(new CreateInvoiceCommand(invoice));
    }

}
