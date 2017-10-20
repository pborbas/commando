package org.commando.command;

import org.commando.result.Result;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public abstract class AbstractCommand<R extends Result> implements Command<R> {

	private static final long serialVersionUID = 1L;

	private final String commandId;
	private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public AbstractCommand() {
		this.commandId = UUID.randomUUID().toString();
	}

	public AbstractCommand(String commandId) {
		if (commandId == null) {
			throw new IllegalArgumentException("Null command ID not allowed");
		}
		this.commandId = commandId;
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
		return (obj!=null && obj.getClass().getName().equals(this.getClass().getName()) && ((AbstractCommand)obj).getCommandId().equals(this.commandId));
	}

	@Override
	public String toString() {
		return "type:"+this.getClass()+" commandId:" + commandId;
	}

	
}
