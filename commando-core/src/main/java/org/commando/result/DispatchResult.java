package org.commando.result;

import org.commando.dispatcher.filter.DispatchFilter;

import java.util.Map;
import java.util.TreeMap;

/**
 * Wrapps a {@link Result} and adds header feature to it. The
 * {@link DispatchFilter}s can use these headers to communicate with each-other
 * (cache info, etc.)
 * 
 * @author pborbas
 *
 */
public class DispatchResult<R extends Result> {
    private final R result;
	private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public DispatchResult(final R result) {
        super();
        this.result = result;
    }

    public R getResult() {
        return this.result;
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

}
