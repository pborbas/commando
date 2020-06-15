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
	 * Where the command was created
	 * @return
	 */
    String getSystem();
	Command<R> setSystem(String system);

		/**
		 * Unique command Id
		 */
    String getCommandId();
	/**
	 * Where the parent command was created
	 * @return
	 */
	String getParentSystem();
	/**
	 * Parent command id. If a command was created during the execution of a parent command.
	 * @return
	 */
	String getParentId();

	/**
	 * Where the origin command was created
	 * @return
	 */
	String getOriginSystem();
	/**
	 * Origin command id. The first command's id in the chain. With this we can track down an execution flow among multiple services
	 * @return
	 */
    String getOriginId();

	public String getHeader(final String headerName);

	public void setHeader(final String headerName, final String value);

	public Map<String, String> getHeaders();


}
