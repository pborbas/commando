package org.commando.spring.boot.actuator.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.commando.dispatcher.MetricsDispatcher;
import org.commando.dispatcher.filter.metrics.DispatcherMetrics.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by pborbas on 30/11/15.
 */
@Component
public class DispatcherMetrics implements MeterBinder {

	private List<MetricsDispatcher> metricsDispatchers;

	@Autowired(required = false)
	public void setMetricsDispatchers(List<MetricsDispatcher> metricsDispatchers) {
		this.metricsDispatchers = metricsDispatchers;
	}

	private void addMetrics(MeterRegistry registry, MetricsDispatcher metricsDispatcher) {
		Metric metric;
		Class<? extends MetricsDispatcher> dispatcherClass = metricsDispatcher.getClass();
		Map<Class, Metric> dispatcherMetrics = metricsDispatcher.getMetrics();
		for (Class commandClass : dispatcherMetrics.keySet()) {
			metric = dispatcherMetrics.get(commandClass);
			String baseName = "dispatcher." + dispatcherClass.getSimpleName() + "." + commandClass.getSimpleName();
			registry.gauge(baseName + ".count", metric.getCount());
			registry.gauge(baseName + ".errorCount", metric.getErrorCount());
			registry.gauge(baseName + ".minTime", metric.getMinTime());
			registry.gauge(baseName + ".avgTime", metric.getAvgTime());
			registry.gauge(baseName + ".maxTime", metric.getMaxTime());
		}
	}

	@Override
	public void bindTo(MeterRegistry registry) {
		for (MetricsDispatcher metricsDispatcher:metricsDispatchers) {
			this.addMetrics(registry, metricsDispatcher);
		}
	}
}
