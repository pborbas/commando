package org.commando.sample.customer;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.jms.dispatch.JmsDispatcher;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;
import org.commando.sample.product.ProductApplication;
import org.commando.sample.product.api.GetProductCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProductApplication.class)
@WebAppConfiguration
public class ProductApplicationTests {

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
	public void getProduct() throws DispatchException {
		dispatcher.dispatchSync(new GetProductCommand(1l)).getValue();
	}
}
