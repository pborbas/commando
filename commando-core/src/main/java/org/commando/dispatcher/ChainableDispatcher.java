package org.commando.dispatcher;

import org.commando.command.Command;
import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;

/**
 * Adds support to a {@link Dispatcher} to continue another dispatcher's work
 * while keeping header informations
 * 
 * @author pborbas
 *
 */
public interface ChainableDispatcher extends Dispatcher {

    /**
     * Executes the command with the dispatcher but instead of using a Command it uses a previously prepared {@link DispatchCommand} which contains headers
     * @throws DispatchException
     */
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand) throws DispatchException;

    public long getTimeout();

}
