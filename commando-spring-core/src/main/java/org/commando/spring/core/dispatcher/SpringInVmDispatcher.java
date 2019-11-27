package org.commando.spring.core.dispatcher;

import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.InVmDispatcher;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.exception.DuplicateActionException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;
import org.commando.spring.core.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Spring version of the {@link InVmDispatcher} which contains spring specific annotations for async execution, transactions and autodetect of {@link DispatchAction} classes
 */
public class SpringInVmDispatcher<A extends Action> extends InVmDispatcher<A> implements Dispatcher {

    public SpringInVmDispatcher() {
        super();
    }

    @Autowired(required = false)
    public SpringInVmDispatcher(final List<DispatchFilter> filters) {
        super(filters);
    }

    @Override
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) {
        return super.dispatch(command);
    }

    @Override
    @Autowired(required = false)
    public void setActions(final List<A> actions) throws DuplicateActionException {
        super.setActions(actions);
    }

}
