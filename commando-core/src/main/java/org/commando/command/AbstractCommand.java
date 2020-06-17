package org.commando.command;

import org.commando.result.Result;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public abstract class AbstractCommand<R extends Result> implements Command<R> {

	private static final long serialVersionUID = 1L;
	public static String NOT_AVAILABLE = "n/a";

	private String system;
	private String parentSystem;
	private String originSystem;
	private final String commandId;
	private String parentId;
	private String originId;
	private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public AbstractCommand() {
		this(generateCommandId(), NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE);
	}

	public AbstractCommand(String commandId) {
		this(commandId, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE, NOT_AVAILABLE);
	}

	public AbstractCommand(Command parentCommand) {
		this(generateCommandId(), parentCommand.getSystem(), parentCommand.getCommandId(),
				parentCommand.getOriginSystem(), parentCommand.getOriginId());
	}

	private AbstractCommand(String commandId, String parentSystem, String parentId, String originSystem,
			String originId) {
		if (commandId == null) {
			throw new IllegalArgumentException("Command ID cannot be null");
		}
		this.system = NOT_AVAILABLE; // dispatcher fills this
		this.parentSystem = parentSystem;
		this.originSystem = originSystem;
		this.commandId = commandId;
		this.parentId = parentId;
		this.originId = (NOT_AVAILABLE.equals(originId)) ? commandId : originId;
	}

	protected static String generateCommandId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String getCommandId() {
		return this.commandId;
	}

	@Override
	public Class<?> getCommandType() {
		return this.getClass();
	}

	public String getHeader(final String headerName) {
		return this.headers.get(headerName);
	}

	public void setHeader(final String headerName, final String value) {
		this.headers.put(headerName, value);
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	@Override
	public int hashCode() {
		return this.getClass().getName().hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		return (obj != null && obj.getClass().getName().equals(this.getClass().getName()) && ((AbstractCommand) obj)
				.getCommandId().equals(this.commandId));
	}
	@Override
	public String getSystem() {
		return system;
	}

	public AbstractCommand<R> setSystem(String system) {
		this.system = system;
		if (NOT_AVAILABLE.equals(originSystem)) {
			originSystem = system;
		}
		return this;
	}

	@Override
	public String getParentSystem() {
		return parentSystem;
	}

	@Override
	public String getOriginSystem() {
		return originSystem;
	}

	public AbstractCommand<R> setOriginSystem(String originSystem) {
		this.originSystem = originSystem;
		return this;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public String getOriginId() {
		return originId;
	}

	public AbstractCommand<R> withParentCommand(Command parent) {
		this.parentSystem = parent.getSystem();
		this.parentId = parent.getCommandId();
		this.originSystem = parent.getOriginSystem();
		this.originId = parent.getOriginId();
		return this;
	}



	@Override
	public String toString() {
		return "{\""+getCommandType().getSimpleName()+"\":{"
				+ "\"origin\":\"" + originSystem + "\""
				+ ", \"originId\":\"" + originId + "\""
				+ ", \"parent\":\"" + parentSystem + "\""
				+ ", \"parentId\":\"" + parentId + "\""
				+ ", \"source\":\"" + system + "\""
				+ ", \"commandId\":\"" + commandId + "\""
				+ "}}";
	}

//	@Override
//	public String toString() {
//		return getCommandType()+"["
//				+ originSystem + "/"
//				+ originId + ", "
//				+ parentSystem + "/"
//				+ parentId + ", "
//				+ system + "/"
//				+ commandId + "] ";
//	}
}
