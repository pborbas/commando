package org.commando.spring.boot.actuator.metrics;

import org.commando.dispatcher.MetricsDispatcher;
import org.commando.dispatcher.filter.metrics.MetricsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by pborbas on 30/11/15.
 */
@Component
public class DispatcherMetrics implements PublicMetrics {

	private List<MetricsDispatcher> metricsDispatchers;

	@Override
	public Collection<Metric<?>> metrics() {
		List<Metric<?>> metrics = new ArrayList<>();
		for (MetricsDispatcher metricsDispatcher:metricsDispatchers) {
			this.addMetrics(metrics, metricsDispatcher);
		}
		return metrics;
	}

	@Autowired(required = false)
	public void setMetricsDispatchers(List<MetricsDispatcher> metricsDispatchers) {
		this.metricsDispatchers = metricsDispatchers;
	}

	private void addMetrics(List<Metric<?>> metrics, MetricsDispatcher metricsDispatcher) {
		MetricsFilter.Metric metric;
		Class<? extends MetricsDispatcher> dispatcherClass = metricsDispatcher.getClass();
		Map<Class, MetricsFilter.Metric> dispatcherMetrics = metricsDispatcher.getMetrics();
		for (Class commandClass : dispatcherMetrics.keySet()) {
			metric = dispatcherMetrics.get(commandClass);
			String baseName = "dispatcher." + dispatcherClass.getSimpleName() + "." + commandClass.getSimpleName();
			metrics.add(new Metric<Number>(baseName + ".count", metric.getCount()));
			metrics.add(new Metric<Number>(baseName + ".errorCount", metric.getErrorCount()));
			metrics.add(new Metric<Number>(baseName + ".minTime", metric.getMinTime()));
			metrics.add(new Metric<Number>(baseName + ".avgTime", metric.getAvgTime()));
			metrics.add(new Metric<Number>(baseName + ".maxTime", metric.getMaxTime()));
		}
	}

}
