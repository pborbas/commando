package org.commando.command;

import org.commando.example.SampleCommand;
import org.commando.example.SampleResult;
import org.junit.Assert;
import org.junit.Test;

public class AbstractCommandTest {

    @SuppressWarnings("serial")
    private class TestCommand extends AbstractCommand<SampleResult> {
		public TestCommand() {
		}

		public TestCommand(String commandId) {
			super(commandId);
		}

		public TestCommand(Command parentCommand) {
			super(parentCommand);
		}
	}

    @Test(expected = IllegalArgumentException.class)
    public void testCommandMustHaveCommandId() {
    	String id=null;
        new TestCommand(id);
    }

    @Test
    public void testEqualsUsingCommandId() {
        TestCommand command1a = new TestCommand("1");
        TestCommand command1b = new TestCommand("1");
        TestCommand command2 = new TestCommand("2");
        Assert.assertEquals(command1a, command1a);
        Assert.assertEquals(command1a, command1b);
        Assert.assertNotEquals(command1a, command2);
    }

    @Test
    public void testCommandTypeIsTheImplementationsType() {
        Assert.assertEquals(SampleCommand.class, new SampleCommand().getCommandType());
    }

    @Test
    public void testHashCodeUsingCommandName() {
        SampleCommand sampleCommand = new SampleCommand();
        TestCommand command1a = new TestCommand(sampleCommand.getCommandId());

        TestCommand command1b = new TestCommand("2");
        Assert.assertEquals(command1a.hashCode(), command1b.hashCode());
        Assert.assertNotEquals(command1a.hashCode(), sampleCommand.hashCode());
    }

	@Test
	public void commandInheritsFromParent() {
    	TestCommand origin = new TestCommand();
    	origin.setSystem("origin");
    	origin.setOriginSystem("origin");
    	TestCommand parent = new TestCommand(origin);
    	parent.setSystem("parent");
    	TestCommand current = new TestCommand(parent);
		current.setSystem("current");
    	Assert.assertEquals(origin.getSystem(), current.getOriginSystem());
    	Assert.assertEquals(origin.getCommandId(), current.getOriginId());
    	Assert.assertEquals(parent.getSystem(), current.getParentSystem());
    	Assert.assertEquals(parent.getCommandId(), current.getParentId());
		System.out.println(current);
	}
}
