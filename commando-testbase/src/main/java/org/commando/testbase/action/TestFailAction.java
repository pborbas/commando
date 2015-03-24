package org.commando.testbase.action;

import org.commando.action.Action;
import org.commando.exception.CommandValidationException;
import org.commando.exception.DispatchException;
import org.commando.result.VoidResult;
import org.commando.testbase.command.TestFailCommand;
import org.commando.testbase.exception.TestDispatchException;

public class TestFailAction implements Action<TestFailCommand, VoidResult>{

    @Override
    public Action<TestFailCommand, VoidResult> validate(final TestFailCommand command) throws CommandValidationException {
        return this;
    }

    @Override
    public VoidResult execute(final TestFailCommand command) throws DispatchException {
        throw new TestDispatchException("This will always fail:(");
    }

    @Override
    public Class<TestFailCommand> getCommandType() {
        return TestFailCommand.class;
    }

}
