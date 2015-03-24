package org.commando.remote.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.DispatchCommand;
import org.commando.dispatcher.ChainableDispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

public class DefaultCommandReceiver implements CommandReceiver {

    private static Log LOGGER = LogFactory.getLog(DefaultCommandReceiver.class);

    private final Serializer serializer;
    private final ChainableDispatcher dispatcher;

    public DefaultCommandReceiver(final Serializer serializer, final ChainableDispatcher dispatcher) {
        super();
        this.serializer = serializer;
        this.dispatcher = dispatcher;
    }

    @Override
    public TextDispatcherResult execute(final TextDispatcherCommand textDispatcherCommand) {
        DispatchCommand dispatchCommand = null;
        try {
            dispatchCommand = this.convertRequest(textDispatcherCommand);
            DispatchResult<? extends Result> dispatchResult = this.dispatcher.dispatch(dispatchCommand).getDispatchResult();
            return this.convertResult(dispatchResult);
        } catch (Throwable e) {
            LOGGER.error("Error while dispatching command: " + e, e);
            return this.createErrorResultMessage(dispatchCommand, e);
        }
    }

    protected DispatchCommand convertRequest(final TextDispatcherCommand textDispatcherCommand) throws CommandSerializationException {
        DispatchCommand dispatchCommand = new DispatchCommand(this.serializer.toCommand(textDispatcherCommand.getTextCommand()));
        dispatchCommand.getHeaders().putAll(textDispatcherCommand.getHeaders());
        return dispatchCommand;
    }

    protected TextDispatcherResult convertResult(final DispatchResult<? extends Result> dispatchResult) throws CommandSerializationException {
        TextDispatcherResult resultMessage = new TextDispatcherResult(dispatchResult.getResult().getCommandId(), this.serializer.toText(dispatchResult.getResult()));
        resultMessage.getHeaders().putAll(dispatchResult.getHeaders());
        return resultMessage;
    }

    protected TextDispatcherResult createErrorResultMessage(final DispatchCommand dispatchCommand, final Throwable t) {
        String commandId = (dispatchCommand == null) ? null : dispatchCommand.getCommand().getCommandId();
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
