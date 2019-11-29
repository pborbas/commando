package org.commando.remote.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.dispatcher.AbstractDispatcher;
import org.commando.dispatcher.filter.Executor;
import org.commando.exception.CommandSerializationException;
import org.commando.exception.DispatchException;
import org.commando.remote.exception.RemoteExceptionUtil;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.NoResult;
import org.commando.result.Result;
import org.commando.util.CommandUtil;

/**
 * Common logic of the {@link RemoteDispatcher}
 */
public abstract class AbstractRemoteDispatcher extends AbstractDispatcher implements RemoteDispatcher, Executor {

    private static final Log LOGGER = LogFactory.getLog(AbstractRemoteDispatcher.class);

    protected final Serializer serializer;

    public AbstractRemoteDispatcher(final Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    @Override
    protected Executor getExecutor() {
        return this;
    }

	//TODO: must test
    @Override
	public <C extends Command<R>, R extends Result> R execute(C dispatchCommand) throws DispatchException{
        TextDispatcherCommand textDispatcherCommand=this.serializeCommand(dispatchCommand);
		LOGGER.debug("Executing remote command: "+textDispatcherCommand.toString(LOGGER.isDebugEnabled()));
        TextDispatcherResult textDispatcherResult=this.executeRemote(dispatchCommand, textDispatcherCommand, this.getTimeout());
        return this.parseResult(dispatchCommand, textDispatcherResult);
    }

    protected <C extends Command<R>, R extends Result> R parseResult(final C command, final TextDispatcherResult textDispatcherResult) throws DispatchException {
        LOGGER.debug("Parsing result after remote execution: "+textDispatcherResult.toString(LOGGER.isDebugEnabled()));
		String exceptionClass = textDispatcherResult.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS);
		if (exceptionClass != null) {
            throw RemoteExceptionUtil.convertToException(textDispatcherResult.getTextResult(),
					exceptionClass);
        }
        R dispatchResult;
        if (CommandUtil.isNoResultCommand(command)) {
            return (R) new NoResult(command.getCommandId());
        } else {
            dispatchResult=(R) this.serializer.toResult(textDispatcherResult.getTextResult());
        }
        dispatchResult.getHeaders().putAll(textDispatcherResult.getHeaders());
        return dispatchResult;
    }

    /**
     * Creates a basic command message which contains all the library specific
     * headers
     * 
     * @throws CommandSerializationException
     */
    protected TextDispatcherCommand serializeCommand(final Command dispatchCommand) throws CommandSerializationException {
        LOGGER.debug("Serializing command before remote execution");
        TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(this.serializer.toText(dispatchCommand));
        textDispatcherCommand.getHeaders().putAll(dispatchCommand.getHeaders());
        return textDispatcherCommand;
    }

    /**
     * Converts the message into its protocoll specific representation (text
     * command body and headers) and sends it through the wire. It receives the
     * response, copies the result and headers into the response
     * 
     * @param command
     *            . The original command
     * @param textDispatchCommand
     *            . Prepared command message with headers and serialized text
     *            command
     * @return
     * @throws DispatchException
     */
    protected abstract TextDispatcherResult executeRemote(Command<? extends Result> command, TextDispatcherCommand textDispatchCommand, Long timeout) throws DispatchException;

}
