package org.commando.remote.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Serialized version of a {@link org.commando.command.Command}
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

	public TextDispatcherCommand(String textCommand, Map<String, String> headers) {
		this.textCommand = textCommand;
		this.headers.putAll(headers);
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
