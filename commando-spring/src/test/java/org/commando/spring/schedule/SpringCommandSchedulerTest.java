package org.commando.spring.schedule;

import org.commando.dispatcher.Dispatcher;
import org.commando.result.Result;
import org.mockito.Mockito;

public class SpringCommandSchedulerTest {

    @SuppressWarnings("unchecked")
    public void testAllScheduledBasedOnScheduledCommads() {
        SpringCommandScheduler commandScheduler = new SpringCommandScheduler();
        commandScheduler.setDispatcher(Mockito.mock(Dispatcher.class));
	ScheduledCommand<?, Result> scheduledCommand = Mockito
		.mock(ScheduledCommand.class);
        //TODO write test
    }

    public void testStartsAutomatically() {
        //TODO write test
    }
}
