package org.commando.remote.http.model;

import org.commando.remote.model.TextDispatcherResult;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Serialized version of the {@link org.commando.result.Result}
 *
 * @author pborbas
 */
public class ReactiveTextDispatcherResult extends TextDispatcherResult {

	private final String commandId;
	private final Mono<String> textResultMono;
	private final Map<String, String> headers = new HashMap<String, String>();

	public ReactiveTextDispatcherResult(final String commandId, Mono<String> textResultMono) {
		super(commandId, null); //we'll use the mono instead
		this.commandId = commandId;
		this.textResultMono = textResultMono;
	}

	public String getCommandId() {
		return this.commandId;
	}

	public String getTextResult() {
		return this.textResultMono.block();
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
		return this.getTextResult();
	}

	public String toString(boolean showContent) {
		String result = toString();
		if (showContent) {
			result += "\nHeaders:";
			for (String headerName : headers.keySet()) {
				result += "\n " + headerName + "=" + headers.get(headerName);
			}
		}
		return result;
	}

}
