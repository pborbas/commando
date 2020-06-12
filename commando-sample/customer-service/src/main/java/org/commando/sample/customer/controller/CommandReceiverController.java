package org.commando.sample.customer.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.exception.DispatchException;
import org.commando.remote.http.receiver.ReactiveCommandReceiver;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.spring.remote.http.model.ResultResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Delegates all requests to the command receiver
 */

@RestController
@RequestMapping("/api/customer")
public class CommandReceiverController {

	private static final Log LOG = LogFactory.getLog(CommandReceiverController.class);

	private final ReactiveCommandReceiver customerCommandReceiver;

	@Autowired
	public CommandReceiverController(ReactiveCommandReceiver customerCommandReceiver) {
		this.customerCommandReceiver = customerCommandReceiver;
	}

	@RequestMapping(value = "/**", method = RequestMethod.POST)
	public Mono<ResponseEntity<String>> dispatch(@RequestBody String textCommand, @RequestHeader Map<String, String> headers)
			throws DispatchException {
		Mono<TextDispatcherResult> resultMono = this.customerCommandReceiver
				.execute(new TextDispatcherCommand(textCommand, headers));
		return resultMono.map(textDispatcherResult -> new ResultResponseEntity(textDispatcherResult));
	}

	/*
	{
					HttpHeaders httpHeaders = new HttpHeaders();
					textDispatcherResult.getHeaders().entrySet()
							.forEach(entry -> httpHeaders.add(entry.getKey(), entry.getValue()));
					ResponseEntity<String> responseEntity = ResponseEntity.ok().headers(httpHeaders)
							.body(textDispatcherResult.getTextResult());
					return responseEntity;
				}
	 */
}
