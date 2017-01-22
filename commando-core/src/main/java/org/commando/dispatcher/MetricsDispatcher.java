package org.commando.dispatcher;

import org.commando.dispatcher.filter.metrics.MetricsFilter;

import java.util.Map;

/**
 * Created by pborbas on 27/02/16.
 */
public interface MetricsDispatcher {
    Map<Class, MetricsFilter.Metric> getMetrics();
}
