package org.commando.core.reactive.dispatcher;

import org.commando.command.Command;
import org.commando.result.Result;
import reactor.core.publisher.Mono;

/**
 * Executes the command or delegates it to another dispatcher
 */
public interface ReactiveExecutor {
    /**
     * Executes the command or delegates it to another dispatcher
     * 
     */
    @SuppressWarnings("rawtypes")
	<C extends Command<R>, R extends Result> Mono<R> execute(C command);// throws DispatchException;

}
