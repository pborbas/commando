package org.commando.sample.customer;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.jms.dispatch.JmsDispatcher;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.billing.BillingApplication;
import org.commando.sample.billing.api.CreateInvoiceCommand;
import org.commando.sample.billing.model.Invoice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BillingApplication.class)
@WebAppConfiguration
public class BillingApplicationTests {

    @Autowired
	Serializer serializer;
    @Autowired
    @Qualifier("commandJmsTemplate")
    JmsTemplate commandJmsTemplate;
    @Autowired
    @Qualifier("resultJmsTemplate")
    JmsTemplate resultJmsTemplate;

    Dispatcher dispatcher;

    @Before
    public void initDispatcher() {
        dispatcher = new JmsDispatcher(commandJmsTemplate, resultJmsTemplate, serializer);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void createInvoice() throws DispatchException {
        CreateInvoiceCommand command = new CreateInvoiceCommand(new Invoice(1l, "inv1", 5000));
        dispatcher.dispatch(command).getResult();
    }
}
