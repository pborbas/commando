package org.commando.remote.http.dispatcher;

import org.commando.example.SampleCommand;
import org.commando.example.SampleResult;
import org.commando.xml.serializer.XmlSerializer;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class ReactiveHttpDispatcherTest {

	@Test
	@Ignore
	public void manualHttpCall() throws Exception {
		ReactiveHttpDispatcher dispatcher = new ReactiveHttpDispatcher("http://localhost:8881/dispatcher/profile/",
				new XmlSerializer());
		SampleResult sampleResult = dispatcher.dispatch(new SampleCommand()).block();
	}
}