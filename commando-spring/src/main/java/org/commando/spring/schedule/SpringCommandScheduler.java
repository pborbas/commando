package org.commando.spring.schedule;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.dispatcher.Dispatcher;
import org.commando.result.Result;
import org.commando.schedule.CommandScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.TaskScheduler;

/**
 * Create an instance of this in your spring configuration and set which DispatcherService you want to use to execute
 * scheduled commands. This class automatically looks for ScheduledCommand implementations (through autowire) and
 * execute them.
 * 
 * @author pborbas
 */
public class SpringCommandScheduler implements CommandScheduler {

    private static final Log LOG = LogFactory.getLog(SpringCommandScheduler.class);

    private Dispatcher dispatcher;
    private List<ScheduledCommand<?, Result>> scheduledCommands;
    private TaskScheduler taskScheduler;
    private boolean autostart;

    private class ScheduledTask implements Runnable {

        private final Dispatcher dispatcher;
        private final ScheduledCommand<?, Result> scheduledCommand;

        protected ScheduledTask(final Dispatcher dispatcher, final ScheduledCommand<?, Result> scheduledCommand) {
            this.dispatcher=dispatcher;
            this.scheduledCommand = scheduledCommand;
        }

        @Override
        public void run() {
            try {
                Result result = this.dispatcher.dispatch(this.scheduledCommand.createCommand()).getResult();
                this.scheduledCommand.handleResult(result);
            } catch (Throwable e) {
                this.scheduledCommand.handleException(e);
            }
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void schedule() {
	for (ScheduledCommand<?, Result> scheduledCommand : this.scheduledCommands) {
            ScheduledTask task = new ScheduledTask(this.dispatcher, scheduledCommand);
            this.taskScheduler.schedule(task, scheduledCommand.createTrigger());
            ScheduledFuture future = this.taskScheduler.schedule(task, scheduledCommand.createTrigger());
            LOG.info("Scheduled: " + scheduledCommand.getClass().getName() + ". First run in: " + future.getDelay(TimeUnit.SECONDS) + " seconds");
        }
    }

    @PostConstruct
    public void autostart() {
        if (this.autostart) {
            this.schedule();
        }
    }

    public void setAutostart(final boolean autostart) {
        this.autostart = autostart;
    }

    @Required
    public void setDispatcher(final Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Required
    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Autowired
    public void setScheduledCommands(
	    final List<ScheduledCommand<?, Result>> scheduledCommands) {
        this.scheduledCommands = scheduledCommands;
    }

}
