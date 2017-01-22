package org.commando.dispatcher.filter;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

import java.util.Iterator;
import java.util.List;

public class DefaultDispatchFilterChain implements DispatchFilterChain {

    private final Iterator<DispatchFilter> filters;
    private final Executor executor;

    public DefaultDispatchFilterChain(final List<DispatchFilter> filters, Executor executor) {
        this.filters = filters.iterator();
	this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @Override
	public <C extends Command<R>, R extends Result> R filter(C dispatchCommand) throws DispatchException {
        if (this.filters.hasNext()) {
            return this.filters.next().filter(dispatchCommand, this);
        }
	return executor.execute(dispatchCommand);
    }
    

}
