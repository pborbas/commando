package org.commando.command;

import org.commando.dispatcher.Dispatcher;
import org.commando.result.Result;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a command sent to the {@link Dispatcher}. It has a specific
 * result type which is returned if the action is successful.
 * 
 * @param <R>
 *            The {@link Result} type.
 */
public interface Command<R extends Result> extends Serializable {

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    /**
     * Required for serialization
     * @return type of implementation class
     */
    Class<?> getCommandType();

    /**
     * Unique command Id
     */
    String getCommandId();

	public String getHeader(final String headerName);

	public void setHeader(final String headerName, final String value);

	public Map<String, String> getHeaders();


}
