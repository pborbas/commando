package org.commando.core.reactive.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.AbstractCommand;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.filter.CommandFilter;
import org.commando.dispatcher.security.NoopSecurityContextManager;
import org.commando.dispatcher.security.SecurityContextManager;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

/**
 * Common logic for all dispatcher implementations
 */
public abstract class AbstractReactiveDispatcher implements ReactiveDispatcher {

	protected final Log LOGGER; //initialized in constructor so it shows the right logger class

	//TODO: reactive: check the usage of filters and add if needed
	//	private final List<DispatchFilter> filters;
	private Long timeout;
	private String system = AbstractCommand.NOT_AVAILABLE;
	private final List<CommandFilter> commandFilters = new LinkedList<>();
	private SecurityContextManager securityContextManager = new NoopSecurityContextManager();

	public AbstractReactiveDispatcher() {
		super();
		this.timeout = DEFAULT_TIMEOUT;
		this.LOGGER = LogFactory.getLog(getClass());
	}

	public SecurityContextManager getSecurityContextManager() {
		return securityContextManager;
	}

	public void setSecurityContextManager(SecurityContextManager securityContextManager) {
		this.securityContextManager = securityContextManager;
	}

	/**
	 * The returned executor will be added to the end of the filter chain.
	 */
	protected abstract ReactiveExecutor getExecutor();

	@Override
	public <C extends Command<R>, R extends Result> Mono<R> dispatch(C command) {
		try {
			//TODO: reactive: forward security context
			if (AbstractCommand.NOT_AVAILABLE.equals(command.getSystem())) {
				command.setSystem(this.system);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Executing command. "+command);
			}
			final long start = System.currentTimeMillis();
			for (CommandFilter filter:this.commandFilters) {
				command = filter.filter(command);
				LOGGER.debug("Filter applied on command: "+filter.getClass().getName());
			}
			final C filteredCommand = command;
			this.addDefaultHeaders(command);
			Mono<R> resultMono = this.getExecutor().execute(command);
			return resultMono.flatMap(result -> {
				result.setCommandId(filteredCommand.getCommandId());
				String executionTime = new Long(System.currentTimeMillis() - start).toString();
				this.addDefaultHeaders(result, executionTime);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Finished command in "
							+ executionTime + "msec. "+filteredCommand);
				}
				//TODO: reactive: clear security context
				return Mono.just(result);
			});
		} catch (DispatchException e) {
			return Mono.error(e);
		}
	}

	public void addCommandFilter(final CommandFilter filter) {
		this.commandFilters.add(filter);
	}

	//TODO: reactive: try to force timeout to returned mono
	protected long getResultTimeout(final Command command) {
		String timeoutHeader = command.getHeader(Dispatcher.HEADER_TIMEOUT);
		Long receivedTimeout = (timeoutHeader != null) ? Long.valueOf(timeoutHeader) : null;
		if (receivedTimeout != null && receivedTimeout < this.timeout) {
			return receivedTimeout;
		}
		return this.timeout;
	}

	protected void addDefaultHeaders(final Command command) {
		command.setHeader(Dispatcher.HEADER_COMMAND_CLASS, command.getCommandType().getName());
		command.setHeader(Dispatcher.HEADER_COMMAND_ID, command.getCommandId());
		command.setHeader(Dispatcher.HEADER_TIMEOUT, this.timeout.toString());
	}

	protected void addDefaultHeaders(final Result result, final String executionTime) {
		result.setHeader(Dispatcher.HEADER_RESULT_CLASS, result.getClass().getName());
		result.setHeader(Dispatcher.HEADER_COMMAND_ID, result.getCommandId());
		result.setHeader(Dispatcher.HEADER_COMMAND_EXECUTION_TIME, executionTime);
	}

	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(final long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "Dispatcher info [Timeout=" + this.getTimeout() + ", Class=" + this.getClass() + "]\n";
	}

	public String getSystem() {
		return system;
	}

	public AbstractReactiveDispatcher setSystem(String system) {
		this.system = system;
		return this;
	}
}
