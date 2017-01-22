package org.commando.dispatcher.filter;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * Filter interface for dispatchers.
 * 
 * @author pborbas
 *
 */
public interface DispatchFilter {
    /**
     * Filters a Command. It throws an exception in case of error. The previous
     * filter can handle the error or delegate it. call filterChain.filter to
     * forward the command to the next filter. Each filter can return a result
     * (cache). The last should execute the command of forward to another
     * dispatcher (remoting)
     * 
     * @throws DispatchException
     */
	<C extends Command<R>, R extends Result>  R filter(C command, DispatchFilterChain filterChain) throws DispatchException;

}
