package org.commando.dispatcher.filter;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * Executes the command or delegates it to another dispatcher
 */
public interface Executor {
    /**
     * Executes the command or delegates it to another dispatcher
     * 
     * @throws DispatchException
     */
    @SuppressWarnings("rawtypes")
	<C extends Command<R>, R extends Result> R execute(C dispatchCommand) throws DispatchException;

}
