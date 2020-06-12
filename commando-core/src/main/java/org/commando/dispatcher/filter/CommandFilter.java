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
public interface CommandFilter {
    /**
     * Filters a Command before dispatch
     * 
     * @throws DispatchException
     */
	<C extends Command<R>, R extends Result> C filter(C command) throws DispatchException;

}
