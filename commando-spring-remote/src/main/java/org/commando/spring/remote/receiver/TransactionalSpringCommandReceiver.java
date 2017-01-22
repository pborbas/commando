package org.commando.spring.remote.receiver;

import org.commando.dispatcher.Dispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by pborbas on 26/06/15.
 */
public class TransactionalSpringCommandReceiver extends DefaultCommandReceiver {

	public TransactionalSpringCommandReceiver(Serializer serializer, Dispatcher dispatcher) {
		super(serializer, dispatcher);
	}

	@Override
	@Transactional
	public TextDispatcherResult execute(TextDispatcherCommand textDispatcherCommand) {
		return super.execute(textDispatcherCommand);
	}
}
