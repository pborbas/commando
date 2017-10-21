package org.commando.sample.gw.resource;

import org.commando.sample.billing.model.Invoice;
import org.commando.sample.customer.api.model.Customer;

/**
 *
 */
public class InvoiceResource {
	private final Customer customer;
	private final Invoice invoice;

	public InvoiceResource(Customer customer, Invoice invoice) {
		this.customer = customer;
		this.invoice = invoice;
	}
}
