package org.commando.result;

import org.commando.exception.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Wrapper for Java {@link CompletableFuture} to force get timeouts and convert Exceptions
 * for more convenient usage.
 *
 * @param <R> Type of result
 * @author pborbas
 */
public class ResultFuture<R extends Result> extends CompletableFuture<R> {

	private final long startTime;
	private final long timeout;

	public ResultFuture(final long timeout) {
		super();
		this.timeout = timeout;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	@Deprecated
	public R get() throws InterruptedException, ExecutionException {
		try {
			return this.get(this.timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			throw new ExecutionException(e);
		}
	}

	public R getResult() throws DispatchException {
		return this.getResult(this.timeout, TimeUnit.MILLISECONDS);
	}

	public R getResult(final long timeout, final TimeUnit unit) throws DispatchException {
		try {
			return this.get(timeout, unit);
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
		if ((!this.isCancelled() && !this.isDone()) && (System.currentTimeMillis() > this.startTime + this.timeout)) {
			this.cancel(true);
		}
	}

}
