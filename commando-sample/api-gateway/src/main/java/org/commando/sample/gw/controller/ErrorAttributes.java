package org.commando.sample.gw.controller;

import org.commando.remote.http.exception.ExceptionWithHttpStatus;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 *	Turns commando exceptions into proper http status codes in WebFlux
 */
//
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		Throwable error = getError(request);
		Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
		if (error instanceof ExceptionWithHttpStatus) {
			errorAttributes.put("status", ((ExceptionWithHttpStatus)error).getHttpStatusCode());
			errorAttributes.put("error", error.getClass());
			errorAttributes.put("message", error.getMessage());
		}
		return errorAttributes;
	}
}
