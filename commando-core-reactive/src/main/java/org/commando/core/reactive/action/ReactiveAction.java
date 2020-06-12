package org.commando.core.reactive.action;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import reactor.core.publisher.Mono;

public interface ReactiveAction<C extends Command<R>, R extends Result> {

	/**
	 * Handles the specified action.
	 *
	 * @throws DispatchException
	 *             if there is a problem performing the specified action.
	 */
	Mono<R> execute(C command) throws DispatchException;

	/**
	 * @return The type of {@link Command} supported by this action.
	 */
	Class<C> getCommandType();

}
