package org.commando.example;

import org.commando.action.AbstractAction;

public class SampleAction extends AbstractAction<SampleCommand, SampleResult> {

    @Override
    public SampleResult execute(final SampleCommand command) {
        return new SampleResult(command.getCommandId());
    }

}
