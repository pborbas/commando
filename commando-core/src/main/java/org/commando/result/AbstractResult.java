package org.commando.result;

import org.commando.command.Command;

import java.util.Map;
import java.util.TreeMap;

public class AbstractResult implements Result {

	private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private String commandId;

    protected AbstractResult() {
        // for serialization
    }
    
    public AbstractResult(final String commandId) {
        if (commandId == null) {
            throw new IllegalArgumentException("Null command ID not allowed");
        }
        this.commandId = commandId;
    }

    public AbstractResult(Command command) {
    	this(command.getCommandId());
	}

    @Override
    public String getCommandId() {
        return this.commandId;
    }

    @Override
    public void setCommandId(final String commandId) {
        this.commandId = commandId;
    }

    @Override
    public String toString() {
        return "type:" + this.getClass() + " commandId:" + this.commandId;
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

}
