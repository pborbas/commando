package org.commando.sample.customer.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.exception.DispatchException;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.receiver.CommandReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Delegates all requests to the command receiver
 */

@RestController
@RequestMapping("/api/customer")
public class CommandReceiverController {

	private static final Log LOG = LogFactory.getLog(CommandReceiverController.class);

	private final CommandReceiver customerCommandReceiver;

	@Autowired
	public CommandReceiverController(CommandReceiver customerCommandReceiver) {
		this.customerCommandReceiver = customerCommandReceiver;
	}

	//TODO: reactive: move this to a commando reactive module
	@RequestMapping(value = "/**", method = RequestMethod.POST)
	public Mono<ResponseEntity<String>> dispatch(@RequestBody String textCommand, @RequestHeader Map<String, String> headers)
			throws DispatchException {
		TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(textCommand, headers);
		CompletableFuture<TextDispatcherResult> resultCompletableFuture = this.customerCommandReceiver
				.execute(textDispatcherCommand);
		Mono<ResponseEntity<String>> responseEntityMono = Mono
				.fromFuture(resultCompletableFuture.thenApply(textDispatcherResult -> {
					HttpHeaders httpHeaders = new HttpHeaders();
					textDispatcherResult.getHeaders().entrySet()
							.forEach(entry -> httpHeaders.add(entry.getKey(), entry.getValue()));
					ResponseEntity<String> responseEntity = ResponseEntity.ok().headers(httpHeaders)
							.body(textDispatcherResult.getTextResult());
					return responseEntity;
				}));
		return responseEntityMono;
	}

}
