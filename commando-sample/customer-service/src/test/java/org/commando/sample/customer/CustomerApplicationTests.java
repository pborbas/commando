package org.commando.sample.customer;

import org.commando.exception.DispatchException;
import org.commando.remote.http.dispatcher.RestHttpDispatcher;
import org.commando.sample.customer.CustomerApplication;
import org.commando.sample.customer.api.CreateCustomerCommand;
import org.commando.sample.customer.api.ListCustomersCommand;
import org.commando.sample.customer.model.Customer;
import org.commando.serializer.Serializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CustomerApplication.class)
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
public class CustomerApplicationTests {

    @Autowired
    Serializer serializer;
    RestHttpDispatcher dispatcher;

    @Before
    public void initDispatcher() {

        dispatcher = new RestHttpDispatcher("http://localhost:8881/dispatcher", serializer);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateCustomer() throws DispatchException {
        dispatcher.dispatch(new CreateCustomerCommand(new Customer(1, "test1"))).getResult();
        Assert.assertEquals(1, dispatcher.dispatch(new ListCustomersCommand()).getResult().getCustomers().size());
    }

}