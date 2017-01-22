package org.commando.dispatcher.filter.retry;

import org.commando.command.Command;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * Created by pborbas on 26/11/15.
 */
public class LimitedRetryFilter implements DispatchFilter {

	private final RetryCommandRepository retryCommandRepository;

	public LimitedRetryFilter(RetryCommandRepository retryCommandRepository) {
		this.retryCommandRepository = retryCommandRepository;
	}

	@Override
	public <C extends Command<R>, R extends Result> R filter(C dispatchCommand, DispatchFilterChain filterChain)
			throws DispatchException {
		if (dispatchCommand instanceof LimitedRetryCommand) {
			LimitedRetryCommand command = (LimitedRetryCommand) dispatchCommand;
			String key = command.getClass().getName() + "/" + command.getRetryKey();
			int failedExecCount = retryCommandRepository.getFailedExecCount(key);
			if (failedExecCount >=command.getMaxRetries()) {
				throw new LimitedRetryException("Command was already failed "+failedExecCount+" times. No more execution allowed.");
			}
			try {
				return filterChain.filter(dispatchCommand);
			} catch (Throwable e) {
				retryCommandRepository.increaseFailedExecCount(key);
				throw e;
			}
		} else {
			return filterChain.filter(dispatchCommand);
		}
	}

}
