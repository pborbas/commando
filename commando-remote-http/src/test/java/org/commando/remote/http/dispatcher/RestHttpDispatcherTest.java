package org.commando.remote.http.dispatcher;

import java.util.concurrent.TimeUnit;

import org.commando.command.Command;
import org.commando.dispatcher.ChainableDispatcher;
import org.commando.example.SampleCommand;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.filter.circuit.CircuitBreakerFilter;
import org.commando.remote.dispatcher.filter.circuit.CircuiteBreakerException;
import org.commando.remote.http.receiver.JettyUnitServlet;
import org.commando.remote.http.receiver.TestHttpReceiverServlet;
import org.commando.result.Result;
import org.commando.testbase.test.AbstractDispatcherTest;
import org.commando.xml.serializer.XmlSerializer;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RestHttpDispatcherTest extends AbstractDispatcherTest {

    static Server server;

    final RestHttpDispatcher dispatcher;

    public RestHttpDispatcherTest() {
        this.dispatcher = new RestHttpDispatcher("http://localhost:8123/", new XmlSerializer());
        this.dispatcher.setTimeout(1000);
    }

    @Test(expected=CircuiteBreakerException.class)
    public void testCircuiteBreaker() throws DispatchException {
        //send to invalid port
        RestHttpDispatcher dispatcher = new RestHttpDispatcher("http://localhost:9999/", new XmlSerializer());
        CircuitBreakerFilter circuitBreakerFilter = new CircuitBreakerFilter();
        circuitBreakerFilter.setErrorThreshold(2);
        circuitBreakerFilter.setOpenInterval(500);
        dispatcher.addFilter(circuitBreakerFilter);
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        this.dispatchWithoutError(dispatcher, new SampleCommand());
        //after 2 failure CB should be in open state
        dispatcher.dispatch(new SampleCommand()).getResult();
    }

    private void dispatchWithoutError(final RestHttpDispatcher dispatcher, final Command<? extends Result> command) {
        try {
            dispatcher.dispatch(command).getResult(5000, TimeUnit.MILLISECONDS);
        } catch (DispatchException e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeClass
    public static void startServer() throws Exception {
        server = JettyUnitServlet.startServer(TestHttpReceiverServlet.class, 8123, "/*");
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Override
    protected ChainableDispatcher getDispatcher() {
        return this.dispatcher;
    }

}