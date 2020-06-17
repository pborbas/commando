package org.commando.dispatcher.filter.metrics;

import org.commando.command.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pborbas on 26/11/15.
 */
public class DispatcherMetrics {

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

	public void success(Command command, long executionTime) {
		Class commandType=command.getCommandType();
		this.getMetric(commandType).success(executionTime);
	}

	public void error(Command command) {
		Class commandType=command.getCommandType();
		this.getMetric(commandType).error();
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
