package org.commando.testbase.dispatcher;

import org.commando.dispatcher.InVmDispatcher;
import org.commando.example.SampleAction;
import org.commando.exception.DuplicateActionException;
import org.commando.testbase.action.NoResultAction;
import org.commando.testbase.action.TestFailAction;
import org.commando.testbase.action.TestWaitAction;
import org.commando.testbase.filter.TestFilter;

public class TestDispatcherFactory {

    public static InVmDispatcher createTestInVmDispatcher() {
        try {
            InVmDispatcher dispatcher = new InVmDispatcher();
            dispatcher.registerAction(new SampleAction());
            dispatcher.registerAction(new TestWaitAction());
            dispatcher.registerAction(new TestFailAction());
            dispatcher.registerAction(new  NoResultAction());
            dispatcher.addFilter(new TestFilter());
            dispatcher.setTimeout(1000);
            return dispatcher;
        } catch (DuplicateActionException e) {
            throw new RuntimeException(e);
        }
    }

}
