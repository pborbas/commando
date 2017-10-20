package org.commando.result;

import org.commando.command.Command;

import java.util.Map;

/**
 * Interface for {@link Command} results.
 * 
 */
public interface Result {
	
	/**
	 * ID of the Command that this Result belogs to. 
	 */
	String getCommandId();
	
	void setCommandId(String commandId);

	String getHeader(final String headerName);

	void setHeader(final String headerName, final String value);

	Map<String, String> getHeaders();

}
