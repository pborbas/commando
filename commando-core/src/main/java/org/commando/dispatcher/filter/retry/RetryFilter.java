package org.commando.dispatcher.filter.retry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

public class RetryFilter implements DispatchFilter {
	private static final Log LOG = LogFactory.getLog(RetryFilter.class);
	private final RetryPolicy retryPolicy;

	public RetryFilter() {
		this.retryPolicy = new DefaultRetryPolicy();
	}

	public RetryFilter(final RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Override
	public <C extends Command<R>, R extends Result> R filter(C dispatchCommand, DispatchFilterChain filterChain)
			throws DispatchException {
		try {
			return filterChain.filter(dispatchCommand);
		} catch (Throwable e) {
			return this.doRetry(0, dispatchCommand, filterChain, e);
		}
	}

	protected <C extends Command<R>, R extends Result> R  doRetry(int retryCount, final C dispatchCommand,
			final DispatchFilterChain filterChain, final Throwable lastException) throws DispatchException {
		retryCount++;
		if (retryCount <= this.retryPolicy.getMaxRetryCount()) {
			long waitTime = this.retryPolicy.getWaitTime(retryCount);
			LOG.error("Error while executing command, retrying after " + waitTime + " msec. Error: " + lastException);
			try {
				Thread.sleep(waitTime);
				LOG.debug("Retrying command. Retry:" + retryCount + "/" + this.retryPolicy.getMaxRetryCount());
				return filterChain.filter(dispatchCommand);
			} catch (Throwable e) {
				return this.doRetry(retryCount, dispatchCommand, filterChain, e);
			}
		}
		LOG.error("All retries failed");
		if (lastException instanceof DispatchException) {
			throw (DispatchException) lastException;
		}
		throw new DispatchException("All retries failed.", lastException);
	}

}
