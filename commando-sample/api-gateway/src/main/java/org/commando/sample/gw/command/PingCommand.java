package org.commando.sample.gw.command;

import org.commando.command.AbstractCommand;

/**
 *
 */
public class PingCommand extends AbstractCommand<PingResult> {
	private final Long delay;

	public PingCommand(Long delay) {
		this.delay = delay;
	}

	public Long getDelay() {
		return delay;
	}
}
