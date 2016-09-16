package org.commando.spring.remote.http.receiver;

import org.commando.remote.http.receiver.AbstractHttpCommandReceiverServlet;
import org.commando.remote.receiver.CommandReceiver;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class SpringCommandReceiverSerlvet extends AbstractHttpCommandReceiverServlet {

	public static final String COMMAND_RECEIVER_ATTRIBUTE = "commandReceiver";
	private CommandReceiver commandReceiver;

	public SpringCommandReceiverSerlvet() {
	}

	public SpringCommandReceiverSerlvet(CommandReceiver commandReceiver) {
		this.commandReceiver = commandReceiver;
	}

	@Override
	protected CommandReceiver initCommandReceiver(ServletConfig config) throws ServletException {
		if (this.commandReceiver == null) {
			String beanName = config.getInitParameter(COMMAND_RECEIVER_ATTRIBUTE);
			if (beanName == null) {
				commandReceiver = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext())
						.getBean(CommandReceiver.class);
			} else {
				commandReceiver = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext())
						.getBean(beanName, CommandReceiver.class);
			}
			return commandReceiver;
		}
		return commandReceiver;
	}
}
