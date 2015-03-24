package org.commando.remote.receiver;

import org.commando.dispatcher.Dispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;

/**
 * Can receive a remotely sent, serialized command and delegate it to the
 * locally configured {@link Dispatcher} for execution
 * 
 * @author pborbas
 *
 */
public interface CommandReceiver {
    /**
     * Takes the text representation of a command, converts it to an Object
     * command with all the headers, executes the command and converts the
     * Result with all the headers into a text result
     * 
     * @param commandMessage
     * @return
     */
    TextDispatcherResult execute(TextDispatcherCommand commandMessage);

    long getTimeout();
}
