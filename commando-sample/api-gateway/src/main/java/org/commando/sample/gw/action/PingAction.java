package org.commando.sample.gw.action;

import org.commando.action.AbstractAction;
import org.commando.sample.gw.command.PingCommand;
import org.commando.sample.gw.command.PingResult;
import org.commando.spring.core.action.DispatchAction;

/**
 *
 */
@DispatchAction
public class PingAction extends AbstractAction<PingCommand, PingResult> {
	@Override
	public PingResult execute(PingCommand command) {
		PingResult pong = new PingResult(command.getCommandId(), "pong");
		if (command.getDelay()!=null) {
			try {
				Thread.sleep(command.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return pong;
	}
}
