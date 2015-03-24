package org.commando.sample.billing.api;

import org.commando.command.AbstractCommand;
import org.commando.result.NoResult;
import org.commando.sample.billing.model.Invoice;

public class CreateInvoiceCommand extends AbstractCommand<NoResult> {

    private final Invoice invoice;

    public CreateInvoiceCommand(final Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }
}
