package org.commando.spring.remote.http.model;

import org.commando.remote.model.TextDispatcherResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultResponseEntity extends ResponseEntity<String> {
	public ResultResponseEntity(TextDispatcherResult textDispatcherResult) {
		super(textDispatcherResult.getTextResult(), HttpStatus.OK);
		textDispatcherResult.getHeaders().entrySet()
				.forEach(entry -> this.getHeaders().add(entry.getKey(), entry.getValue()));
	}
}
