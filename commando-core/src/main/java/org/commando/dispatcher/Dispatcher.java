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
     * Finds the {@link Action} for the specified {@link Command} and executes
     * it. Returns the appropriate {@link Result}.
     * 
     * @return The action's result.
     * @throws DispatchException
     *             if the action execution failed.
     */
    <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command) throws DispatchException;

}
