package org.commando.spring.remote.http.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.remote.http.receiver.HttpCommandReceiver;
import org.commando.spring.remote.http.receiver.SpringCommandReceiverSerlvet;
import org.springframework.boot.context.embedded.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.List;

/**
 * Registers a servlet to all HttpCommandReceiver beans
 */
public class HttpCommandReceiverServletRegistration implements ServletContextInitializer {

	private final Log LOG= LogFactory.getLog(HttpCommandReceiverServletRegistration.class);

	private final List<HttpCommandReceiver> httpCommandReceivers;

	public HttpCommandReceiverServletRegistration(List<HttpCommandReceiver> httpCommandReceivers) {
		this.httpCommandReceivers = httpCommandReceivers;
	}


	protected int getLoadOnStartup() {
		return 1;
	}

	protected boolean isAsyncSupported() {
		return true;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		for (HttpCommandReceiver httpCommandReceiver:httpCommandReceivers) {
			String servletName = httpCommandReceiver.getServletName();
			ServletRegistration.Dynamic registration = servletContext
					.addServlet(servletName, new SpringCommandReceiverSerlvet(httpCommandReceiver));
			if (registration == null) {
				LOG.info("Servlet " + servletName + " was not registered " + "(possibly already registered?)");
			} else {
				registration.setAsyncSupported(isAsyncSupported());
				registration.addMapping(httpCommandReceiver.getMappingUrl());
				registration.setLoadOnStartup(getLoadOnStartup());
				LOG.info("Command receiver HTTP Servlet: '" + servletName + "' mapped to " + httpCommandReceiver.getMappingUrl());

			}
		}

	}
}
