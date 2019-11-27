package org.commando.remote.http.dispatcher;

import org.commando.example.SampleCommand;
import org.commando.xml.serializer.XmlSerializer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ReactiveHttpDispatcherTest {

	@Test
	public void manualHttpCall() throws Exception {
		ReactiveHttpDispatcher dispatcher = new ReactiveHttpDispatcher("https://localhost:8084/dispatcher/profile/",
				new XmlSerializer());
		dispatcher.dispatchSync(new SampleCommand()).getValue();
	}
}