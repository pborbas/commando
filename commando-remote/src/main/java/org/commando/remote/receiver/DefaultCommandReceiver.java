package org.commando.remote.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.commando.result.ResultFuture;

import java.util.concurrent.CompletableFuture;

public class DefaultCommandReceiver implements CommandReceiver {

	private static Log LOGGER = LogFactory.getLog(DefaultCommandReceiver.class);

	private final Serializer serializer;
	private final Dispatcher dispatcher;

	public DefaultCommandReceiver(final Serializer serializer, final Dispatcher dispatcher) {
		super();
		this.serializer = serializer;
		this.dispatcher = dispatcher;
	}

	@Override
	public CompletableFuture<TextDispatcherResult> execute(final TextDispatcherCommand textDispatcherCommand)
			throws CommandSerializationException {
		LOGGER.debug("Command received: " + textDispatcherCommand.toString(LOGGER.isDebugEnabled()));
		Command command = this.convertRequest(textDispatcherCommand);
		ResultFuture<Result> dispatchFuture = this.dispatcher.dispatch(command);
		CompletableFuture<TextDispatcherResult> textDispatcherResultCompletableFuture = dispatchFuture
				.thenApply(result -> {
					try {
						TextDispatcherResult textDispatcherResult = convertResult(result);
						LOGGER.debug("Response created:"+textDispatcherResult.toString(LOGGER.isDebugEnabled()));
						return textDispatcherResult;
					} catch (CommandSerializationException e) {
						return createErrorResultMessage(command, e);
					}
				});
		return textDispatcherResultCompletableFuture;
	}

	protected Command convertRequest(final TextDispatcherCommand textDispatcherCommand)
			throws CommandSerializationException {
		Command dispatchCommand = this.serializer.toCommand(textDispatcherCommand.getTextCommand());
		dispatchCommand.getHeaders().putAll(textDispatcherCommand.getHeaders());
		return dispatchCommand;
	}

	protected TextDispatcherResult convertResult(final Result dispatchResult) throws CommandSerializationException {
		TextDispatcherResult resultMessage = new TextDispatcherResult(dispatchResult.getCommandId(),
				this.serializer.toText(dispatchResult));
		resultMessage.getHeaders().putAll(dispatchResult.getHeaders());
		return resultMessage;
	}

	protected TextDispatcherResult createErrorResultMessage(final Command dispatchCommand, final Throwable t) {
		String commandId = (dispatchCommand == null) ? null : dispatchCommand.getCommandId();
		TextDispatcherResult resultMessage = new TextDispatcherResult(commandId, t.getMessage());
		resultMessage.setHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS, t.getClass().getName());
		return resultMessage;
	}

	@Override
	public long getTimeout() {
		return this.dispatcher.getTimeout();
	}

	@Override
	public String toString() {
		return this.dispatcher.toString();
	}
}
