package org.commando.dispatcher;

import org.commando.command.Command;
import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;
import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by pborbas on 21/05/15.
 */
public class TestDispatcher implements ChainableDispatcher {

	@Override
	public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand)
			throws DispatchException {
		throw new UnsupportedOperationException("Not yet implemented for tests");
	}

	@Override
	public long getTimeout() {
		return 60000;
	}

	private List<Command> executedCommands = new LinkedList<>();
	private Map<String, Result> resultForCommandIds = new HashMap<>();
	private Map<Class<? extends Command>, Result> resultForCommandTypes = new HashMap<>();
	private Map<Class<? extends Command>, DispatchException> exceptionForCommandTypes = new HashMap<>();
	private Map<Command, Result> resultForCommands = new HashMap<>();

	public void registerResult(String commandId, Result result) {
		this.resultForCommandIds.put(commandId, result);
	}

	public void registerResult(Class<? extends Command> commandType, Result result) {
		this.resultForCommandTypes.put(commandType, result);
	}

	public void registerResult(Command command, Result result) {
		this.resultForCommands.put(command, result);
	}

	@Override
	public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command) throws DispatchException {
		this.executedCommands.add(command);
		R result = null;
		if (this.resultForCommandIds.containsKey(command.getCommandId())) {
			result = (R) this.resultForCommandIds.get(command.getCommandId());
		} else if (this.resultForCommands.containsKey(command)) {
			result = (R) this.resultForCommands.get(command);
		} else if (this.resultForCommandTypes.containsKey(command.getCommandType())) {
			result = (R) this.resultForCommandTypes.get(command.getCommandType());
		} else if (this.exceptionForCommandTypes.containsKey(command.getCommandType())) {
			throw this.exceptionForCommandTypes.get(command.getCommandType());
		}
		if (result == null) {
			throw new DispatchException(
					"No result registered for commandId:" + command.getCommandId() + ", type:" + command
							.getCommandType());
		}
		return new TestResultFuture<>(result);
	}

	@Override
	public <C extends Command<R>, R extends Result> R dispatchSync(C command) throws DispatchException {
		return this.dispatch(command).getResult();
	}

	public List<Command> getExecutedCommands() {
		return executedCommands;
	}

	public <C extends Command> C assertExecutedOnce(Class<C> commandType) {
		C foundCommand = null;
		for (Command command : this.executedCommands) {
			if (command.getCommandType().equals(commandType)) {
				if (foundCommand != null) {
					Assert.fail("Executed commands contained more than one command of type:" + commandType);
				}
				foundCommand = (C) command;
			}
		}
		if (foundCommand == null) {
			Assert.fail("Command never executed with type:" + commandType);
		}
		return foundCommand;
	}

	public void registerException(Class<? extends Command> commandType, DispatchException e) {
		this.exceptionForCommandTypes.put(commandType, e);
	}
}
