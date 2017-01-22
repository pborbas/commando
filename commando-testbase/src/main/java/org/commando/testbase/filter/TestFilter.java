package org.commando.testbase.filter;

import org.commando.command.Command;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.DispatchFilterChain;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

public class TestFilter implements DispatchFilter {

    public static final String HEADER_TEST_FILTER = "testFilterHeader";
    public static final String HEADER_VALUE = "123124124124124";

    @Override
    public <C extends Command<R>, R extends Result>  R filter(C command, DispatchFilterChain filterChain) throws DispatchException {
	R dispatchResult = filterChain.filter(command);
	dispatchResult.setHeader(HEADER_TEST_FILTER, HEADER_VALUE);
	return dispatchResult;
    }
}
