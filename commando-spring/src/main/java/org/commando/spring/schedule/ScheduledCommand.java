package org.commando.spring.schedule;

import org.commando.command.Command;
import org.commando.result.Result;
import org.springframework.scheduling.Trigger;

/**
 * Implementations will be scheduled by CommandScheduler
 * 
 * @author pborbas
 *
 */
public interface ScheduledCommand<C extends Command<R>, R extends Result> {

    /**
     * Create the command which will be executed
     */
    C createCommand();
    
    /**
     * Callback after each execution
     * @param result
     */
    void handleResult(R result);
    
    /**
     * Callback if the action of the command throws an exception
     * @param throwable
     */
    void handleException(Throwable throwable);
    
    /**
     * Creates the trigger which defines the timing of the command execution
     */
    Trigger createTrigger();
    
    
}
