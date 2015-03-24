package org.commando.command;

import java.util.HashMap;
import java.util.Map;

import org.commando.dispatcher.filter.DispatchFilter;
import org.commando.result.Result;

/**
 * Wrapps a {@link Command} and adds header feature to it. The
 * {@link DispatchFilter}s can use these headers to communicate with each-other
 * (cache info, etc.)
 * 
 * @author pborbas
 *
 */
public class DispatchCommand {
    private final Command<? extends Result> command;
    private final Map<String, String> headers = new HashMap<String, String>();

    public DispatchCommand(final Command<? extends Result> command) {
	super();
	this.command = command;
    }

    public Command<? extends Result> getCommand() {
	return this.command;
    }

    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R extends Result> C getCommand(final Class<C> commandType) {
	return (C) this.command;
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
