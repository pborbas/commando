package org.commando.spring.remote.http.model;

import org.commando.remote.model.TextDispatcherResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultResponseEntity extends ResponseEntity<String> {

	public ResultResponseEntity(TextDispatcherResult textDispatcherResult, HttpHeaders headers) {
		super(textDispatcherResult.getTextResult(), headers, HttpStatus.OK);
	}
}
