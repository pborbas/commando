package org.commando.remote.http.dispatcher;

import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.example.SampleCommand;
import org.commando.exception.DispatchException;
import org.commando.json.serializer.JsonSerializer;
import org.commando.remote.dispatcher.filter.circuit.CircuitBreakerFilter;
import org.commando.remote.dispatcher.filter.circuit.CircuiteBreakerException;
import org.commando.remote.http.receiver.JettyUnitServlet;
import org.commando.remote.http.receiver.TestHttpReceiverServlet;
import org.commando.result.Result;
import org.commando.testbase.test.AbstractDispatcherTest;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class HttpDispatcherTest extends AbstractDispatcherTest {

    static Server server;

    final HttpDispatcher dispatcher;

    public HttpDispatcherTest() {
        this.dispatcher = new HttpDispatcher("http://localhost:8123/", new JsonSerializer());
        this.dispatcher.setTimeout(1000);
    }

	@Test
	public void testRemoteCallWithUTF8Encoding() throws DispatchException {
		String data="öüóŐú";
		SampleCommand command=new SampleCommand(data);
		String resultData=this.dispatcher.dispatch(command).getResult(10000, TimeUnit.MILLISECONDS).getValue();
		Assert.assertEquals(data, resultData);

	}

    @Test(expected=CircuiteBreakerException.class)
    public void testCircuiteBreaker() throws DispatchException {
        //send to invalid port
        HttpDispatcher dispatcher = new HttpDispatcher("http://localhost:9999/", new JsonSerializer());
        CircuitBreakerFilter circuitBreakerFilter = new CircuitBreakerFilter();
        circuitBreakerFilter.setErrorThreshold(2);
        circuitBreakerFilter.setOpenInterval(500);
        dispatcher.addFilter(circuitBreakerFilter);
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        //after 2 failure CB should be in open state
        dispatcher.dispatch(new SampleCommand()).getResult();
    }

    private void dispatchWithoutError(final HttpDispatcher dispatcher, final Command<? extends Result> command) {
        try {
            dispatcher.dispatch(command).getResult(5000, TimeUnit.MILLISECONDS);
        } catch (DispatchException e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeClass
    public static void startServer() throws Exception {
        server = JettyUnitServlet.startServer(TestHttpReceiverServlet.class, 8123, "/*");
		Thread.sleep(1000);
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Override
    protected Dispatcher getDispatcher() {
        return this.dispatcher;
    }

}
