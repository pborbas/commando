package org.commando.dispatcher.filter;

import java.util.Iterator;
import java.util.List;

import org.commando.command.DispatchCommand;
import org.commando.exception.DispatchException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

public class DefaultDispatchFilterChain implements DispatchFilterChain {

    private final Iterator<DispatchFilter> filters;
    private final Executor executor;

    public DefaultDispatchFilterChain(final List<DispatchFilter> filters, Executor executor) {
        this.filters = filters.iterator();
	this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(dispatchCommand, this);
        }
	return executor.execute(dispatchCommand);
    }
    

}
