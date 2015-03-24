package org.commando.testbase.command;

import org.commando.command.AbstractCommand;
import org.commando.result.VoidResult;

public class TestWaitCommand extends AbstractCommand<VoidResult> {
    private static final long serialVersionUID = 1L;
    long waitTime;

    public TestWaitCommand(final long waitTime) {
        super();
        this.waitTime = waitTime;
    }

    public long getWaitTime() {
        return this.waitTime;
    }

}
