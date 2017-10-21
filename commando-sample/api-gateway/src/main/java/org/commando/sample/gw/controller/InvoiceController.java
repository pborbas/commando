package org.commando.sample.gw.controller;

import org.commando.sample.billing.dispatcher.BillingDispatcher;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {

    private final CustomerDispatcher customerDispatcher;
    private final BillingDispatcher billingDispatcher;

	@Autowired
	public InvoiceController(CustomerDispatcher customerDispatcher, BillingDispatcher billingDispatcher) {
		this.customerDispatcher = customerDispatcher;
		this.billingDispatcher = billingDispatcher;
	}


}
