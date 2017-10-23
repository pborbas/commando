package org.commando.sample.product.dispatcher;

import org.commando.remote.jms.dispatch.JmsDispatcher;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class ProductJmsDispatcher extends JmsDispatcher implements ProductDispatcher {

	public ProductJmsDispatcher(JmsTemplate commandJmsTemplate, JmsTemplate resultJmsTemplate,
			Serializer serializer) {
		super(commandJmsTemplate, resultJmsTemplate, serializer);
	}

}
