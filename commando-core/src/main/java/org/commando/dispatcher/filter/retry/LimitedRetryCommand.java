package org.commando.dispatcher.filter.retry;

/**
 * Created by pborbas on 19/02/16.
 */
public interface LimitedRetryCommand {

	String getRetryKey();
	int getMaxRetries();

}
