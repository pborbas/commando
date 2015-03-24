package org.commando.dispatcher.filter.retry;

public interface RetryPolicy {
    int getMaxRetryCount();
    long getWaitTime(int retryCount);
}       
