package org.commando.remote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Serialized version of the {@link org.commando.result.Result}
 * 
 * @author pborbas
 *
 */
public class TextDispatcherResult {
    
    private final String commandId;
    private final String textResult;
    private final Map<String, String> headers = new HashMap<String, String>();

    public TextDispatcherResult(final String commandId, final String textResult) {
        this.textResult=textResult;
	this.commandId = commandId;
    }

    public String getCommandId() {
	return this.commandId;
    }

    public String getTextResult() {
        return this.textResult;
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
		return textResult;
	}

	public String toString(boolean showContent) {
		if (showContent) {
			String result=toString();
			result+="\nHeaders:";
			for (String headerName:headers.keySet()) {
				result+="\n "+headerName+"="+headers.get(headerName);
			}
			return result;
		}
		return "Content hidden.";
	}

}
