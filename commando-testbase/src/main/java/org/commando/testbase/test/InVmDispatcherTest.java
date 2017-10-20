package org.commando.testbase.test;

import org.commando.action.Action;
import org.commando.command.AbstractCommand;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.InVmDispatcher;
import org.commando.dispatcher.filter.retry.DefaultRetryPolicy;
import org.commando.dispatcher.filter.retry.RetryFilter;
import org.commando.exception.CommandValidationException;
import org.commando.exception.DispatchException;
import org.commando.result.VoidResult;
import org.commando.testbase.dispatcher.TestDispatcherFactory;
import org.junit.Assert;
import org.junit.Test;

public class InVmDispatcherTest extends AbstractDispatcherTest {

    private final InVmDispatcher dispatcher = TestDispatcherFactory.createTestInVmDispatcher();

    @Override
    protected Dispatcher getDispatcher() {
	return this.dispatcher;
    }

    public static class CallCountCommand extends AbstractCommand<VoidResult> {
	private static final long serialVersionUID = 1L;
	final boolean fail;

	public CallCountCommand(final boolean fail) {
	    super();
	    this.fail = fail;
	}

    }

    public static class CallCountAction implements Action<CallCountCommand, VoidResult> {
	int count = 0;

	@Override
	public Action<CallCountCommand, VoidResult> validate(final CallCountCommand command) throws CommandValidationException {
	    return this;
	}

	@Override
	public VoidResult execute(final CallCountCommand command) throws DispatchException {
	    this.count++;
	    if (command.fail) {
		throw new DispatchException("Failed cause you asked for it");
	    }
	    return new VoidResult(command.getCommandId());
	}

	@Override
	public Class<CallCountCommand> getCommandType() {
	    return CallCountCommand.class;
	}

	public int getCount() {
	    return this.count;
	}
    }

    @Test
    public void testRetry() throws DispatchException {
	InVmDispatcher inVmDispatcher = new InVmDispatcher();
	inVmDispatcher.setTimeout(30000);
	CallCountAction action = new CallCountAction();
	inVmDispatcher.registerAction(action);
	DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy();
	retryPolicy.setBaseWaitTime(10);
	retryPolicy.setMaxRetryCount(3);
	retryPolicy.setMultiplier(2);
	inVmDispatcher.addFilter(new RetryFilter(retryPolicy));
	try {
	    inVmDispatcher.dispatch(new CallCountCommand(true)).getResult();
	} catch (DispatchException e) {
	    // ok
	}
	Assert.assertEquals(4, action.getCount());
    }

}
