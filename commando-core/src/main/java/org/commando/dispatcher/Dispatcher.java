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

    public static final String HEADER_TIMEOUT = "commandTimeout";
    public static final String HEADER_COMMAND_ID = "commandId";
    public static final String HEADER_COMMAND_CLASS = "commandClass";
    public static final String HEADER_COMMAND_EXECUTION_TIME= "commandExecutionTime";
    public static final String HEADER_RESULT_CLASS = "resultClass";

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
