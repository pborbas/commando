package org.commando.testbase.action;

import org.commando.action.Action;
import org.commando.exception.CommandValidationException;
import org.commando.exception.DispatchException;
import org.commando.result.VoidResult;
import org.commando.testbase.command.TestWaitCommand;

public class TestWaitAction implements Action<TestWaitCommand, VoidResult>{

    @Override
    public Action<TestWaitCommand, VoidResult> validate(final TestWaitCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public VoidResult execute(final TestWaitCommand command) throws DispatchException {
        try {
            Thread.sleep(command.getWaitTime());
        } catch (InterruptedException e) {
	    // nop
        }
        return new VoidResult(command.getCommandId());
    }

    @Override
    public Class<TestWaitCommand> getCommandType() {
        return TestWaitCommand.class;
    }

}
