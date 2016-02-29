package org.commando.dispatcher;

import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.command.DispatchCommand;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.Executor;
import org.commando.exception.ActionNotFoundException;
import org.commando.exception.DispatchException;
import org.commando.exception.DuplicateActionException;
import org.commando.result.DispatchResult;
import org.commando.result.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InVm implementation of the {@link Dispatcher} Registers itself as the last {@link DispatchFilter} in the chain as an
 * executor filter.
 */
public class InVmDispatcher<A extends Action> extends AbstractDispatcher implements Dispatcher, Executor {

    private Map<Class<?>, A> actionsMap = new HashMap<>();

    public InVmDispatcher() {
        super();
    }

    public InVmDispatcher(final List<DispatchFilter> filters) {
        super(filters);
    }

    @Override
    protected Executor getExecutor() {
        return this;
    }

    @Override
    public DispatchResult<Result> execute(final DispatchCommand dispatchCommand) throws DispatchException {
        Result result = this.executeInVm(dispatchCommand.getCommand());
        DispatchResult<Result> dispatchResult = new DispatchResult<Result>(result);
        return dispatchResult;
    }

    protected <C extends Command<R>, R extends Result> R executeInVm(final C command) throws DispatchException {
        return this.findAction(command).validate(command).execute(command);
    }

    @SuppressWarnings("unchecked")
    protected <C extends Command<R>, R extends Result> Action<C, R> findAction(final C command) throws ActionNotFoundException {
        if (this.actionsMap.containsKey(command.getClass())) {
            return (Action<C, R>) this.actionsMap.get(command.getClass());
        }
        throw new ActionNotFoundException("Action not found for command:" + command);
    }

    public void setActions(final List<A> actions) throws DuplicateActionException {
        this.actionsMap = new HashMap<>();
        for (A action : actions) {
            this.registerAction(action);
        }
    }

    public void registerAction(final A action) throws DuplicateActionException {
        if (this.actionsMap.containsKey(action.getCommandType())) {
            throw new DuplicateActionException("Duplicate action:" + action.getClass().getName() + " for command type:" + action.getCommandType());
        }
        LOGGER.info("Action:" + action.getClass().getName() + " registered for command type:" + action.getCommandType());
        this.actionsMap.put(action.getCommandType(), action);
    }

    @Override
    public String toString() {
        String info = "Dispatcher info [Timeout=" + this.getTimeout() + ", Class=" + this.getClass() + "]\n";
        info += "Actions:\n";
        if (this.actionsMap != null) {
            for ( Action<? extends Command<? extends Result>, ? extends Result> action : this.actionsMap.values()) {
                info += "  -Command: " + action.getCommandType().getName() + " mapped to action: " + action + "\n";
            }
        }
        info += "Filters:\n";
        if (this.getFilters() != null) {
            for (DispatchFilter filter : this.getFilters()) {
                info+="  -Filter:"+filter.getClass().getName()+". "+filter.toString();
            }
        }
        return info;
    }

}
