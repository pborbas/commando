package org.commando.sample.customer;

import org.commando.exception.DispatchException;
import org.commando.remote.http.dispatcher.RestHttpDispatcher;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.customer.api.command.CreateCustomerCommand;
import org.commando.sample.customer.api.command.ListCustomersCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerApplicationTests {
	@LocalServerPort
	int randomServerPort;

    @Autowired
	Serializer serializer;
    RestHttpDispatcher dispatcher;

    @Before
    public void initDispatcher() {

        dispatcher = new RestHttpDispatcher("http://localhost:"+randomServerPort+"/api/customer/", serializer);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateCustomer() throws DispatchException {
        dispatcher.dispatch(new CreateCustomerCommand("test1")).getResult();
        Assert.assertEquals(1, dispatcher.dispatch(new ListCustomersCommand()).getResult().getCustomers().size());
    }

}
