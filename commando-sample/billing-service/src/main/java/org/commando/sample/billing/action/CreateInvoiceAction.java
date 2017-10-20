package org.commando.sample.billing.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.action.AbstractAction;
import org.commando.exception.DispatchException;
import org.commando.result.NoResult;
import org.commando.sample.billing.api.CreateInvoiceCommand;
import org.commando.sample.billing.model.Invoice;
import org.commando.sample.billing.repo.InvoiceRepository;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DispatchAction
public class CreateInvoiceAction extends AbstractAction<CreateInvoiceCommand, NoResult>{

    private final Log LOG=LogFactory.getLog(CreateInvoiceAction.class);

    @Autowired
    InvoiceRepository InvoiceRepository;

    @Override
    @Transactional
    public NoResult execute(final CreateInvoiceCommand createInvoiceCommand) throws DispatchException {
        Invoice createdInvoice=this.InvoiceRepository.save(createInvoiceCommand.getInvoice());
        this.LOG.info("invoice created:"+createdInvoice.getId());
        return new NoResult(createInvoiceCommand.getCommandId());
    }

}
