package org.commando.dispatcher;

import org.commando.command.Command;
import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;
import org.junit.Assert;

import java.util.*;

/**
 * Created by pborbas on 21/05/15.
 */
public class TestDispatcher implements ChainableDispatcher {

	@Override
	public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand)
			throws DispatchException {
		throw new UnsupportedOperationException("Not yet implemented for tests");
	}

	public interface ResultSelector<C extends Command<R>, R extends Result> {
		boolean isResultFor(C command);
		R getResult(C command) throws DispatchException;
	}

	@Override
	public long getTimeout() {
		return 60000;
	}

	private final List<ResultSelector> registeredResults = Collections.synchronizedList(new ArrayList<ResultSelector>());
	private List<Command> executedCommands = Collections.synchronizedList(new LinkedList<Command>());

	/**
	 * Result for commandId
	 *
	 * @param commandId
	 * @param result
	 */
	public void registerResult(final String commandId, final Result result) {
		this.registeredResults.add(new ResultSelector() {
			@Override
			public boolean isResultFor(Command command) {
				return commandId.equals(command.getCommandId());
			}

			@Override
			public Result getResult(Command command) {
				return result;
			}
		});
	}

	/**
	 * Result for command class type
	 *
	 * @param commandType
	 * @param result
	 */
	public void registerResult(final Class<? extends Command> commandType, final Result result) {
		this.registeredResults.add(new ResultSelector() {
			@Override
			public boolean isResultFor(Command command) {
				return command.getClass().equals(commandType);
			}

			@Override
			public Result getResult(Command command) {
				return result;
			}
		});
	}

	public void registerException(final Class<? extends Command> commandType, final DispatchException e) {
		this.registeredResults.add(new ResultSelector() {
			@Override
			public boolean isResultFor(Command command) {
				return command.getClass().equals(commandType);
			}

			@Override
			public Result getResult(Command command) throws DispatchException {
				throw e;
			}
		});
	}

	/**
	 * Result for command.equals()
	 *
	 * @param command
	 * @param result
	 */
	public void registerResult(final Command command, final Result result) {
		this.registeredResults.add(new ResultSelector() {
			@Override
			public boolean isResultFor(Command currentCommand) {
				return command.equals(currentCommand);
			}

			@Override
			public Result getResult(Command command) {
				return result;
			}
		});
	}


	/**
	 * Result for selector. Use this for any custom logic like if the command.getSpecificMethod returns this or that.
	 * @param resultSelector
	 */
	public void registerResult(ResultSelector resultSelector) {
		this.registeredResults.add(resultSelector);
	}

	@Override
	public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command) throws DispatchException {
		this.executedCommands.add(command);
		R result = null;
		for (ResultSelector rs : this.registeredResults) {
			if (rs.isResultFor(command)) {
				result = (R) rs.getResult(command);
			}
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

	public <C extends Command> List<C> getExecutedCommands(Class<C> commandType) {
		List<C> executeList = new LinkedList<>();
		for (Command command : this.executedCommands) {
			if (command.getCommandType().equals(commandType)) {
				executeList.add((C) command);
			}
		}
		return executeList;
	}

	public <C extends Command> C assertExecutedOnce(Class<C> commandType) {
		List<C> executeList = this.assertExecutionCount(commandType, 1);
		return executeList.get(0);
	}

	public <C extends Command> List<C> assertExecutionCount(Class<C> commandType, int expectedCount) {
		List<C> executeList = this.getExecutedCommands(commandType);
		if (executeList.size() != expectedCount) {
			Assert.fail("Command of type:" + commandType + " was executed " + executeList.size() + " times instead of "
					+ expectedCount);
		}
		return executeList;
	}

}
