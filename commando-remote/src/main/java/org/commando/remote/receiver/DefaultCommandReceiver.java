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
    public TextDispatcherResult execute(final TextDispatcherCommand textDispatcherCommand) {
        Command dispatchCommand = null;
        try {
            dispatchCommand = this.convertRequest(textDispatcherCommand);
            Result dispatchResult = this.dispatcher.dispatch(dispatchCommand).getResult();
            return this.convertResult(dispatchResult);
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) LOGGER.warn(e, e); else LOGGER.warn(e);
            return this.createErrorResultMessage(dispatchCommand, e);
        }
    }

    protected Command convertRequest(final TextDispatcherCommand textDispatcherCommand) throws CommandSerializationException {
        Command dispatchCommand = this.serializer.toCommand(textDispatcherCommand.getTextCommand());
        dispatchCommand.getHeaders().putAll(textDispatcherCommand.getHeaders());
        return dispatchCommand;
    }

    protected TextDispatcherResult convertResult(final Result dispatchResult) throws CommandSerializationException {
        TextDispatcherResult resultMessage = new TextDispatcherResult(dispatchResult.getCommandId(), this.serializer.toText(dispatchResult));
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
