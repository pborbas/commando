package org.commando.dispatcher;

import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.result.Result;

/**
 * dispatcher lifecycle callback methods
 * @author pborbas
 *
 */
public interface DispatcherCallback {
    
    void onError(Command<? extends Result> command, DispatchException e) throws DispatchException;

    void onSuccess(Command<? extends Result> command, Result result) throws DispatchException;
}
