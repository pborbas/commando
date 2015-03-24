package org.commando.testbase.filter;

import org.commando.command.DispatchCommand;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

public class TestFilter implements DispatchFilter {

    public static final String HEADER_TEST_FILTER = "testFilterHeader";
    public static final String HEADER_VALUE = "123124124124124";

    @Override
    public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain) throws DispatchException {
	DispatchResult<? extends Result> dispatchResult = filterChain.filter(dispatchCommand);
	dispatchResult.setHeader(HEADER_TEST_FILTER, HEADER_VALUE);
	return dispatchResult;
    }
}
