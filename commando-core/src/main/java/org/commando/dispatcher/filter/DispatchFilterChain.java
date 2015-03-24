package org.commando.dispatcher.filter;

import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

public interface DispatchFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
    public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand) throws DispatchException;

}
