package org.commando.result;

import org.commando.command.Command;

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
}
