package org.commando.dispatcher;

import org.commando.dispatcher.filter.metrics.DispatcherMetrics;

import java.util.Map;

/**
 * Created by pborbas on 27/02/16.
 */
public interface MetricsDispatcher {
    Map<Class, DispatcherMetrics.Metric> getMetrics();
}
