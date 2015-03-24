package org.commando.result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.DispatcherCallback;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.exception.AsyncErrorException;
import org.commando.exception.AsyncInterruptedException;
import org.commando.exception.AsyncTimeoutException;
import org.commando.exception.DispatchException;
import org.commando.exception.ExceptionUtil;

/**
 * Wrapper for Java {@link Future} to force get timeouts and convert Exceptions
 * for more convenient usage. Besides the concrete {@link Result} it gives
 * access to the {@link Dispatcher}'s {@link DispatchResult} which contains
 * headers of local and/or remote {@link DispatchFilter}s
 * 
 * @author pborbas
 * @param <R>
 *            Type of result
 */
public class ResultFuture<R extends Result> implements Future<R>, DispatcherCallback {

    private final long startTime;
    private final long timeout;
    private Future<DispatchResult<R>> wrappedFuture;
    private R result;
    private DispatchException exception;
    private ResultCallback<R> callback;

    public ResultFuture(final long timeout) {
	super();
	this.timeout = timeout;
	this.startTime = System.currentTimeMillis();
    }

    public ResultFuture(final Future<DispatchResult<R>> wrappedFuture, final long timeout) {
	this.wrappedFuture = wrappedFuture;
	this.timeout = timeout;
	this.startTime = System.currentTimeMillis();
    }

    public void setWrappedFuture(final Future<DispatchResult<R>> wrappedFuture) {
	this.wrappedFuture = wrappedFuture;
    }

    public void registerCallback(final ResultCallback<R> callback) {
	this.callback = callback;
	this.notifyCallback();
    }

    @Override
    public void onError(final Command<? extends Result> command, final DispatchException e) throws DispatchException {
	this.exception = e;
	this.notifyCallback();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(final Command<? extends Result> command, final Result result) throws DispatchException {
	this.result = (R) result;
	this.notifyCallback();
    }

    private void notifyCallback() {
	if (this.callback != null) {
	    if (this.result != null) {
		this.callback.onSuccess(this.result);
	    }
	    if (this.exception != null) {
		this.callback.onError(this.exception);
	    }
	}
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
	return this.wrappedFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
	return this.wrappedFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
	return this.wrappedFuture.isDone();
    }

    @Override
    @Deprecated
    /**
     * use getResult instead
     */
    public R get() throws InterruptedException, ExecutionException {
	try {
	    return this.wrappedFuture.get(this.timeout, TimeUnit.MILLISECONDS).getResult();
	} catch (TimeoutException e) {
	    throw new ExecutionException(e);
	}
    }

    @Override
    @Deprecated
    /**
     * use getResult instead
     */
    public R get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
	return this.wrappedFuture.get(timeout, unit).getResult();
    }

    public R getResult() throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
	return this.getResult(this.timeout, TimeUnit.MILLISECONDS);
    }

    public R getResult(final long timeout, final TimeUnit unit) throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
	return this.getDispatchResult(timeout, unit).getResult();
    }

    public DispatchResult<R> getDispatchResult() throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
	return this.getDispatchResult(this.timeout, TimeUnit.MILLISECONDS);
    }

    public DispatchResult<R> getDispatchResult(final long timeout, final TimeUnit unit) throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
	try {
	    return this.wrappedFuture.get(timeout, unit);
	} catch (InterruptedException e) {
	    throw new AsyncInterruptedException("Execution interrupted:" + e, e);
	} catch (ExecutionException e) {
	    DispatchException dispatchException = ExceptionUtil.findDispatchException(e, 0);
	    if (dispatchException != null) {
		throw dispatchException;
	    }
	    throw new AsyncErrorException("Execution error:" + e, e);
	} catch (TimeoutException e) {
	    throw new AsyncTimeoutException("Execution timed out:" + e, e);
	} finally {
	    this.killOnTimeout();
	}
    }

    private void killOnTimeout() {
	if ((!this.wrappedFuture.isCancelled() && !this.wrappedFuture.isDone()) && (System.currentTimeMillis() > this.startTime + this.timeout)) {
	    this.wrappedFuture.cancel(true);
	}
    }

}
