package org.commando.sample.gw.command;

import org.commando.result.StringResult;

/**
 *
 */
public class PingResult extends StringResult {
	public PingResult(String commandId, String value) {
		super(commandId, value);
	}
}
