package org.commando.spring.core.dispatcher;

import java.util.List;

import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.InVmDispatcher;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.exception.DispatchException;
import org.commando.exception.DuplicateActionException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring version of the {@link InVmDispatcher} which contains spring specific annotations for async execution, transactions and autodetect of {@link DispatchAction} classes
 */
public class SpringInVmDispatcher extends InVmDispatcher implements Dispatcher {

    public SpringInVmDispatcher() {
        super();
    }

    @Autowired(required = false)
    public SpringInVmDispatcher(final List<DispatchFilter> filters) {
        super(filters);
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) throws DispatchException {
        return super.dispatch(command);
    }

    @Override
    @Autowired(required = false)
    public void setActions(final List<Action<? extends Command<? extends Result>, ? extends Result>> actions) throws DuplicateActionException {
        super.setActions(actions);
    }

}
