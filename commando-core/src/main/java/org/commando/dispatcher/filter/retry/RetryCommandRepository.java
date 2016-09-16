package org.commando.dispatcher.filter.retry;

/**
 *Stores Command retries
 *
 * //TODO: add default invm implementation for Commando lib
 */
public interface RetryCommandRepository {
	int getFailedExecCount(String retryKey) ;
	int increaseFailedExecCount(String retryKey);
}
