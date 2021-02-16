package org.commando.remote.http.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.core.reactive.dispatcher.ReactiveDispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import reactor.core.publisher.Mono;

public class DefaultReactiveCommandReceiver implements ReactiveCommandReceiver {

	private static Log LOGGER = LogFactory.getLog(DefaultReactiveCommandReceiver.class);

	private final Serializer serializer;
	private final ReactiveDispatcher dispatcher;

	public DefaultReactiveCommandReceiver(final Serializer serializer, final ReactiveDispatcher dispatcher) {
		super();
		this.serializer = serializer;
		this.dispatcher = dispatcher;
	}

	@Override
	public Mono<TextDispatcherResult> execute(final TextDispatcherCommand textDispatcherCommand)
			throws DispatchException {
		LOGGER.debug("Command received. " + textDispatcherCommand.toString(LOGGER.isTraceEnabled()));
		Command command = this.convertRequest(textDispatcherCommand); //TODO: reactive: add as reactive step
		Mono<Result> resultMono = this.dispatcher.dispatch(command);
		return resultMono.map(result -> {
			try {
				TextDispatcherResult textDispatcherResult = convertResult(result);
				LOGGER.debug("Response created. "+textDispatcherResult.toString(LOGGER.isTraceEnabled()));
				return textDispatcherResult;
			} catch (CommandSerializationException e) {
				return createErrorResultMessage(command, e);
			}
		}).onErrorResume(e -> {

			return Mono.just(createErrorResultMessage(command, e));
		});
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
	public String toString() {
		return this.dispatcher.toString();
	}
}
