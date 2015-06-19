package org.commando.remote.model;

import org.commando.command.DispatchCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Serialized version of a {@link DispatchCommand}
 * 
 * @author pborbas
 *
 */
public class TextDispatcherCommand {

    private final String textCommand;
    private final Map<String, String> headers = new HashMap<String, String>();

    public TextDispatcherCommand(final String textCommand) {
	this.textCommand = textCommand;
    }

    public String getTextCommand() {
	return this.textCommand;
    }

    public String getHeader(final String headerName) {
	return this.headers.get(headerName);
    }

    public void setHeader(final String headerName, final String value) {
	this.headers.put(headerName, value);
    }

    public Map<String, String> getHeaders() {
	return this.headers;
    }

	@Override
	public String toString() {
		return textCommand;
	}

	public String toString(boolean debug) {
		String result=toString();
		if (debug) {
			result+="\nHeaders:";
			for (String headerName:headers.keySet()) {
				result+="\n "+headerName+"="+headers.get(headerName);
			}
		}
		return result;
	}
}
