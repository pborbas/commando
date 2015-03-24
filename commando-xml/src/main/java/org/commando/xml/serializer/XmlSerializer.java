package org.commando.xml.serializer;

import org.commando.command.Command;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;

import com.thoughtworks.xstream.XStream;

public class XmlSerializer implements Serializer {

    private XStream xstream = new XStream();

    @Override
    public String toText(Command<?> command) {
        return this.xstream.toXML(command);
    }

    @Override
    public Command<?> toCommand(String textCommand) {
        return (Command<?>) this.xstream.fromXML(textCommand);
    }

    public void setXstream(XStream xstream) {
        this.xstream = xstream;
    }

    @Override
    public Result toResult(String textResult) throws CommandSerializationException {
	try {
	    return (Result) this.xstream.fromXML(textResult);
	} catch (Exception e) {
	    throw new CommandSerializationException("Cannot parse text to Result: " + textResult);
	}
    }

    @Override
    public String toText(Result result) throws CommandSerializationException {
	return this.xstream.toXML(result);
    }
}
