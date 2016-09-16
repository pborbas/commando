package org.commando.dispatcher.filter.throttle;

/**
 * Implementation commands will be throttled by {@link ThrottleFilter}
 */
public interface ThrottledCommand {

	String getThrottleKey();
	long getThrottleTimeBetweenCallsInMillisecond();

}
