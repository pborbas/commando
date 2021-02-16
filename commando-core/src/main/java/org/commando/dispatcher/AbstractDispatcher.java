package org.commando.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.AbstractCommand;
import org.commando.command.Command;
import org.commando.dispatcher.filter.DefaultDispatchFilterChain;
import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.dispatcher.filter.Executor;
import org.commando.dispatcher.filter.metrics.DispatcherMetrics;
import org.commando.dispatcher.security.NoopSecurityContextManager;
import org.commando.dispatcher.security.SecurityContextManager;
import org.commando.exception.DispatchException;
import org.commando.result.Result;
import org.commando.result.ResultFuture;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Common logic for all dispatcher implementations
 */
public abstract class AbstractDispatcher implements Dispatcher {

	protected final Log LOGGER; //initialized in constructor so it shows the right logger class

	private ExecutorService executorService;
	private final List<DispatchFilter> filters;
	private Long timeout;
	private String system = AbstractCommand.NOT_AVAILABLE;
	private SecurityContextManager securityContextManager = new NoopSecurityContextManager();
	private final DispatcherMetrics metrics = new DispatcherMetrics();

	public AbstractDispatcher() {
		this(new LinkedList<>());
	}

	public AbstractDispatcher(final List<DispatchFilter> filters) {
		super();
		this.filters = new LinkedList<>();
		this.filters.addAll(filters);
		this.executorService = new ForkJoinPool();
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
	protected abstract Executor getExecutor();

	@Override
	public <C extends Command<R>, R extends Result> R dispatchSync(C command) throws DispatchException {
		return this.executeCommonWorkflow(command);
	}

	@Override
	public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) {
		if (AbstractCommand.NOT_AVAILABLE.equals(command.getSystem())) {
			command.setSystem(this.system);
		}
		final ResultFuture<R> result = new ResultFuture<R>(this.getResultTimeout(command));
		final Object securityContextInfo = this.securityContextManager.getSecurityContext();
		ResultFuture.runAsync(() -> {
			try {
				securityContextManager.setSecurityContext(securityContextInfo);
				R wfResult = AbstractDispatcher.this.executeCommonWorkflow(command);
				result.complete(wfResult);
			} catch (DispatchException | RuntimeException e) {
				logException(e, command);
				result.completeExceptionally(e);
			} finally {
				securityContextManager.clearSecurityContext();
			}
		}, this.executorService);
		return result;
	}

	protected void logException(Exception e, Command dispatchCommand) {
		LOGGER.error("Error while executing command: " + dispatchCommand + ": " + e, e);
	}

	protected long getResultTimeout(final Command command) {
		String timeoutHeader = command.getHeader(HEADER_TIMEOUT);
		Long receivedTimeout = (timeoutHeader != null) ? Long.valueOf(timeoutHeader) : null;
		if (receivedTimeout != null && receivedTimeout < this.timeout) {
			return receivedTimeout;
		}
		return this.timeout;
	}

	@SuppressWarnings("unchecked")
	public <C extends Command<R>, R extends Result> R executeCommonWorkflow(final C command)
			throws DispatchException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Executing command." + command);
		}
		long start = System.currentTimeMillis();
		R result;
		try {
			this.addDefaultHeaders(command);
			result = new DefaultDispatchFilterChain(this.filters, this.getExecutor()).filter(command);
			result.setCommandId(command.getCommandId());
			Long executionTime = new Long(System.currentTimeMillis() - start);
			this.addDefaultHeaders(result, executionTime.toString());
			LOGGER.info("Finished command in " + executionTime + "msecs. "+command);
			metrics.success(command, executionTime);
			return result;
		} catch (DispatchException e) {
			throw e;
		} catch (Throwable e) {
			metrics.error(command);
			DispatchException dispatchException = new DispatchException("Error while executing command:" + command + ". Message: " + e,
					e);
			throw dispatchException;
		}
	}

	protected void addDefaultHeaders(final Command dispatchCommand) {
		dispatchCommand.setHeader(HEADER_COMMAND_CLASS, dispatchCommand.getCommandType().getName());
		dispatchCommand.setHeader(HEADER_COMMAND_ID, dispatchCommand.getCommandId());
		dispatchCommand.setHeader(HEADER_TIMEOUT, this.timeout.toString());
	}

	protected void addDefaultHeaders(final Result dispatchResult, final String executionTime) {
		dispatchResult.setHeader(HEADER_RESULT_CLASS, dispatchResult.getClass().getName());
		dispatchResult.setHeader(HEADER_COMMAND_ID, dispatchResult.getCommandId());
		dispatchResult.setHeader(HEADER_COMMAND_EXECUTION_TIME, executionTime);
	}

	public List<DispatchFilter> getFilters() {
		return this.filters;
	}

	public void addFilter(final DispatchFilter filter) {
		this.getFilters().add(filter);
	}

	public void setExecutorService(final ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(final long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		String info = "Dispatcher info [Timeout=" + this.getTimeout() + ", Class=" + this.getClass() + "]\n";
		info += "Filters:\n";
		if (this.getFilters() != null) {
			for (DispatchFilter filter : this.getFilters()) {
				info += "  -Filter:" + filter.getClass().getName() + ". " + filter.toString();
			}
		}
		return info;
	}

	public String getSystem() {
		return system;
	}

	public AbstractDispatcher setSystem(String system) {
		this.system = system;
		return this;
	}

	public DispatcherMetrics getMetrics() {
		return metrics;
	}
}
