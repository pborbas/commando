package org.commando.dispatcher.filter.metrics;

import org.commando.command.Command;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * Created by pborbas on 26/11/15.
 */
//TODO: test this
//TODO: refactor storage behind repository interface
public class MetricsFilter implements DispatchFilter {

	private final DispatcherMetrics metrics = new DispatcherMetrics();

	@Override
	public <C extends Command<R>, R extends Result>  R filter(C dispatchCommand, DispatchFilterChain filterChain) throws DispatchException {
		R dispatchResult = null;
		try {
			long start= System.currentTimeMillis();
			dispatchResult = filterChain.filter(dispatchCommand);
			long end=System.currentTimeMillis();
			this.metrics.success(dispatchCommand, end-start);
		} catch (DispatchException e) {
			this.metrics.error(dispatchCommand);
			throw e;
		}
		return dispatchResult;
	}

	public DispatcherMetrics getMetrics() {
		return metrics;
	}
}
