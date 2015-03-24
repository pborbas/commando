package org.commando.testbase.action;

import org.commando.action.Action;
import org.commando.exception.CommandValidationException;
import org.commando.exception.DispatchException;
import org.commando.result.NoResult;
import org.commando.testbase.command.NoResultCommand;

public class NoResultAction implements Action<NoResultCommand, NoResult>{

    @Override
    public Action<NoResultCommand, NoResult> validate(final NoResultCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public NoResult execute(final NoResultCommand command) throws DispatchException {
        return new NoResult(command.getCommandId());
    }

    @Override
    public Class<NoResultCommand> getCommandType() {
        return NoResultCommand.class;
    }

}
