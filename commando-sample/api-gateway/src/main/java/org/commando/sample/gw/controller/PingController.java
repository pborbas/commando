package org.commando.sample.gw.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.dispatcher.InVmDispatcher;
import org.commando.example.SampleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/pings")
public class PingController {
	private static final Log LOG = LogFactory.getLog(PingController.class);
	private final InVmDispatcher testDispatcher;

	@Autowired
	public PingController(InVmDispatcher testDispatcher) {
		this.testDispatcher = testDispatcher;
	}

	@RequestMapping(value = "/command/mono", method = RequestMethod.GET)
	public Mono<String> commandMono(@RequestParam(required = false) Long delay) {
		Mono<String> testMono = Mono.fromFuture(this.testDispatcher.dispatch(new SampleCommand()))
				.map(customerResult -> customerResult.getValue());
		if (delay==null) {
			return testMono;
		} else {
			return testMono.delayElement(Duration.ofMillis(delay));
		}
	}

	@RequestMapping(value = "/string/mono", method = RequestMethod.GET)
	public Mono<String> stringMono(@RequestParam(required = false) Long delay) {
		Mono<String> testMono = Mono.just("pong");
		if (delay==null) {
			return testMono;
		} else {
			return testMono.delayElement(Duration.ofMillis(delay));
		}
	}
}
