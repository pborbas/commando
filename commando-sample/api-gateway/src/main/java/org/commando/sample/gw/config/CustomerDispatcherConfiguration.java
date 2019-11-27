package org.commando.sample.gw.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.action.Action;
import org.commando.command.Command;
import org.commando.dispatcher.InVmDispatcher;
import org.commando.example.SampleResult;
import org.commando.exception.DispatchException;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.commando.sample.customer.api.dispatcher.CustomerDispatcher;
import org.commando.sample.customer.api.dispatcher.CustomerHttpDispatcher;
import org.commando.xml.serializer.XmlSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Configures the dispatchers of the module
 */
@Configuration
public class CustomerDispatcherConfiguration {
private static final Log LOG = LogFactory.getLog(CustomerDispatcherConfiguration.class);

	@Bean
	public Serializer customerSerializer() {
		return new XmlSerializer();
	}

	@Bean
	public InVmDispatcher<Action> inVmDelayedDispatcher() {
		InVmDispatcher<Action> inVmDispatcher = new InVmDispatcher<Action>() {
			@Override
			public <C extends Command<R>, R extends Result> R execute(C dispatchCommand) throws DispatchException {
				SampleResult sampleResult = new SampleResult("1", "Test");
				try {
					LOG.info("blocking thread in test dispatcher");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return (R) sampleResult;
			}
		};
		inVmDispatcher.setExecutorService(Executors.newFixedThreadPool(100));
		return inVmDispatcher;
	}

	@Bean
	public CustomerDispatcher customerDispatcher() {

//		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
//		connManager.setMaxTotal(100);
//		connManager.setDefaultMaxPerRoute(100);
//		HttpClientBuilder clientbuilder = HttpClients.custom().setConnectionManager(connManager);
//		CloseableHttpClient httpclient = clientbuilder.build();
		CustomerHttpDispatcher dispatcher = new CustomerHttpDispatcher("http://localhost:8881/api/customer",
				customerSerializer());
//		dispatcher.setExecutorService(Executors.newFixedThreadPool(100));
		return dispatcher;
	}

}
