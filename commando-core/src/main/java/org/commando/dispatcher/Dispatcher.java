package org.commando.dispatcher;

import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;

/**
 * Executes actions and returns the results.
 * 
 */
public interface Dispatcher {
    public static final long DEFAULT_TIMEOUT = 30000;

    public static final String HEADER_TIMEOUT = "Command-Timeout";
    public static final String HEADER_COMMAND_ID = "Command-Id";
    public static final String HEADER_COMMAND_CLASS = "Command-Class";
    public static final String HEADER_COMMAND_EXECUTION_TIME= "Command-Exec-Time";
    public static final String HEADER_RESULT_CLASS = "Result-Class";

    /**
     * Depending on implementation delegates the command remotely or
	 * Finds the {@link Action} for the specified {@link Command} and executes
     * it asynchronously. Returns the appropriate {@link Result}.
     * 
     * @return The action's result as a CompletableFuture
     */
    <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command);

	/**
	 * Depending on implementation delegates the command remotely or
	 * finds the {@link Action} for the specified {@link Command} and executes
	 * it synchronously. Returns the appropriate {@link Result}.
	 *
	 * @return The action's result
	 */
	<C extends Command<R>, R extends Result>  R dispatchSync(C command) throws DispatchException;
	long getTimeout();

}
