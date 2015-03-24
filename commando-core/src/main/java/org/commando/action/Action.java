package org.commando.action;

import org.commando.command.Command;
import org.commando.exception.CommandValidationException;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * Instances of this interface will handle specific types of {@link Command} classes.
 */
public interface Action<C extends Command<R>, R extends Result> {

    /**
     * If command is valid do nothing just return the action (this). If something is wrong throw {@link CommandValidationException} with the details
     * 
     * @throws CommandValidationException
     */
    Action<C, R> validate(C command) throws CommandValidationException;

    /**
     * Handles the specified action.
     * 
     * @throws DispatchException
     *             if there is a problem performing the specified action.
     */
    R execute(C command) throws DispatchException;

    /**
     * @return The type of {@link Command} supported by this action.
     */
    Class<C> getCommandType();

}
