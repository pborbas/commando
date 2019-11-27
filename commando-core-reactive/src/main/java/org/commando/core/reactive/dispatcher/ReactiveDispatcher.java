package org.commando.core.reactive.dispatcher;

import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import reactor.core.publisher.Mono;

/**
 * Executes actions and returns the results.
 * 
 */
public interface ReactiveDispatcher {
    public static final long DEFAULT_TIMEOUT = 30000;

    /**
     * Depending on implementation delegates the command remotely or
	 * Finds the {@link Action} for the specified {@link Command} and executes
     * it asynchronously. Returns the appropriate {@link Result}.
     * 
     * @return The action's result as a Mono
     */
    <C extends Command<R>, R extends Result> Mono<R> dispatch(C command) throws DispatchException;

}
