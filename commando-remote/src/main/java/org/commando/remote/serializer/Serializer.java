package org.commando.remote.serializer;

import org.commando.command.Command;
import org.commando.exception.CommandSerializationException;
import org.commando.result.Result;

/**
 * Convert a command/result to its text representation and back to object
 * @author pborbas
 *
 */
public interface Serializer {

    String toText(Command<?> command) throws CommandSerializationException;

    String toText(Result result) throws CommandSerializationException;

    Command<?> toCommand(String textCommand) throws CommandSerializationException;

    Result toResult(String textResult) throws CommandSerializationException;

    String getContentType();
    String getCharset();
}
