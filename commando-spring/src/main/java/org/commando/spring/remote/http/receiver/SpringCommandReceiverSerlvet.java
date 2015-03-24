package org.commando.spring.remote.http.receiver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.commando.remote.http.receiver.AbstractHttpCommandReceiverServlet;
import org.commando.remote.receiver.CommandReceiver;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class SpringCommandReceiverSerlvet extends AbstractHttpCommandReceiverServlet {

    public static final String COMMAND_RECEIVER_ATTRIBUTE = "commandReceiver";

    @Override
    protected CommandReceiver initCommandReceiver(ServletConfig config) throws ServletException {
	CommandReceiver commandReceiver;
	String beanName = config.getInitParameter(COMMAND_RECEIVER_ATTRIBUTE);
        if (beanName == null) {
	    commandReceiver = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(CommandReceiver.class);
        } else {
	    commandReceiver = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext()).getBean(beanName, CommandReceiver.class);
        }
	return commandReceiver;
    }

}
