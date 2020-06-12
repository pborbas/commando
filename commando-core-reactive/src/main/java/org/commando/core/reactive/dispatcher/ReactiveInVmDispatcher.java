package org.commando.core.reactive.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.core.reactive.action.ReactiveAction;
import org.commando.exception.ActionNotFoundException;
import org.commando.exception.DispatchException;
import org.commando.exception.DuplicateActionException;
import org.commando.result.Result;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 *
 */
public class ReactiveInVmDispatcher<A extends ReactiveAction> implements ReactiveDispatcher {

	private static final Log LOG = LogFactory.getLog(ReactiveInVmDispatcher.class);
	private Map<String, A> actionsMap = new HashMap<>();
	private Scheduler scheduler;

	public ReactiveInVmDispatcher() {
		this.scheduler = Schedulers.parallel();
	}

	public ReactiveInVmDispatcher(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public ReactiveInVmDispatcher(ExecutorService executorService) {
		this.scheduler = Schedulers.fromExecutorService(executorService);
	}

	@Override
	public <C extends Command<R>, R extends Result> Mono<R> dispatch(C command) throws DispatchException {
		return this.findAction(command).execute(command).publishOn(this.scheduler);
	}

	public void setActions(final List<A> actions) throws DuplicateActionException {
		this.actionsMap = new HashMap<>();
		for (A action : actions) {
			this.registerAction(action);
		}
	}

	public void registerAction(final A action) throws DuplicateActionException {
		if (this.actionsMap.containsKey(action.getCommandType().getName())) {
			throw new DuplicateActionException("Duplicate action:" + action.getClass().getName() + " for command type:" + action.getCommandType());
		}
		LOG.info("Action:" + action.getClass().getName() + " registered for command type:" + action.getCommandType());
		this.actionsMap.put(action.getCommandType().getName(), action);
	}

	@SuppressWarnings("unchecked")
	protected <C extends Command<R>, R extends Result> ReactiveAction<C, R> findAction(final C command) throws
			ActionNotFoundException {
		if (this.actionsMap.containsKey(command.getClass().getName())) {
			return (ReactiveAction<C, R>) this.actionsMap.get(command.getClass().getName());
		}
		throw new ActionNotFoundException("Action not found for command:" + command);
	}


}
