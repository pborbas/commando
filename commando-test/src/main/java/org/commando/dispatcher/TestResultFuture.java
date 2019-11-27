package org.commando.dispatcher;

import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by pborbas on 23/04/15.
 */
public class TestResultFuture<R extends Result> extends ResultFuture<R> {

	private final R result;

	public TestResultFuture(R result) {
		super(0);
		this.result = result;
	}

	@Override
	@Deprecated
	public R get() throws InterruptedException, ExecutionException {
		return result;
	}

	@Override
	@Deprecated
	public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return result;
	}

	@Override
	public R getResult() throws DispatchException {
		return result;
	}

	@Override
	public R getResult(long timeout, TimeUnit unit) throws DispatchException {
		return result;
	}

}
