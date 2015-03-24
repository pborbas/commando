package org.commando.dispatcher.filter;

import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.DispatchResult;

/**
 * 
 * @author pborbas
 *
 */
public interface Executor {
    /**
     * Executes the command or delegates it to another dispatcher
     * 
     * @throws DispatchException
     */
    @SuppressWarnings("rawtypes")
    public DispatchResult execute(DispatchCommand dispatchCommand) throws DispatchException;

}
