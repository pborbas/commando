package org.commando.dispatcher.filter.metrics;

import org.commando.command.DispatchCommand;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pborbas on 26/11/15.
 */
//TODO: test this
//TODO: refactor storage behind repository interface
public class MetricsFilter implements DispatchFilter {

	public static class Metric {
		long count;
		long errorCount;
		long minTime;
		long maxTime;
		long avgTime;

		void success(long time) {
			if (minTime==0 || time<minTime) {
				minTime=time;
			}
			if (maxTime==0 || maxTime<time) {
				maxTime=time;
			}
			avgTime=(count*avgTime+time)/(count+1);
			count++;
		}

		void error() {
			errorCount++;
		}

		public long getCount() {
			return count;
		}

		public long getErrorCount() {
			return errorCount;
		}

		public long getMinTime() {
			return minTime;
		}

		public long getMaxTime() {
			return maxTime;
		}

		public long getAvgTime() {
			return avgTime;
		}
	}

	private final Map<Class, Metric> metrics=new HashMap<>();

	@Override
	public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain)
			throws DispatchException {
		DispatchResult<? extends Result> dispatchResult = null;
		Class commandType=dispatchCommand.getCommand().getCommandType();
		try {
			long start= System.currentTimeMillis();
			dispatchResult = filterChain.filter(dispatchCommand);
			long end=System.currentTimeMillis();
			this.getMetric(commandType).success(end-start);
		} catch (DispatchException e) {
			this.getMetric(commandType).error();
			throw e;
		}
		return dispatchResult;
	}

	private Metric getMetric(Class commandType) {
		Metric metric=this.metrics.get(commandType);
		if (metric==null) {
			metric=new Metric();
			metrics.put(commandType,metric);
		}
		return metric;
	}

	public Map<Class, Metric> getMetrics() {
		return metrics;
	}
}
