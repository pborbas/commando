package org.commando.sample.billing.dispatcher;

import org.commando.remote.jms.dispatch.JmsDispatcher;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.serializer.Serializer;

/**
 *
 */
public class BillingJmsDispatcher extends JmsDispatcher implements BillingDispatcher {

	public BillingJmsDispatcher(JmsTemplate commandJmsTemplate, JmsTemplate resultJmsTemplate,
			Serializer serializer) {
		super(commandJmsTemplate, resultJmsTemplate, serializer);
	}

}
