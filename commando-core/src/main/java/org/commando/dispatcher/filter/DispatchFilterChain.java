package org.commando.dispatcher.filter;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

public interface DispatchFilterChain {
    /**
     * Calls the next filter in the chain
     * @throws DispatchException
     */
	<C extends Command<R>, R extends Result>  R filter(C command) throws DispatchException;


}
