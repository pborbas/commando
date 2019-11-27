package org.commando.spring.remote.receiver;

import org.commando.dispatcher.Dispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * Created by pborbas on 26/06/15.
 */
@Deprecated
public class TransactionalSpringCommandReceiver extends DefaultCommandReceiver {

	public TransactionalSpringCommandReceiver(Serializer serializer, Dispatcher dispatcher) {
		super(serializer, dispatcher);
	}

	@Override
	@Transactional
	public CompletableFuture<TextDispatcherResult> execute(TextDispatcherCommand textDispatcherCommand)
			throws CommandSerializationException {
		return super.execute(textDispatcherCommand);
	}
}
