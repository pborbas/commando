package org.commando.sample.gw.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.sample.gw.command.PingCommand;
import org.commando.sample.gw.command.PingResult;
import org.commando.sample.gw.dispatcher.GwDispatcher;
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
	private final GwDispatcher gwDispatcher;

	@Autowired
	public PingController(GwDispatcher gwDispatcher) {
		this.gwDispatcher = gwDispatcher;
	}

	@RequestMapping(value = "/command/mono", method = RequestMethod.GET)
	public Mono<String> commandMono(@RequestParam(required = false) Long delay) {
		Mono<PingResult> pingResultMono = Mono.fromFuture(this.gwDispatcher.dispatch(new PingCommand(delay)));
		Mono<String> testMono = pingResultMono.map(pingResult -> pingResult.getValue());
		return testMono;
	}

	@RequestMapping(value = "/string/mono", method = RequestMethod.GET)
	public Mono<String> stringMono(@RequestParam(required = false) Long delay) {
		Mono<String> testMono = Mono.just("pong");
		if (delay == null) {
			return testMono;
		} else {
			return testMono.delayElement(Duration.ofMillis(delay));
		}
	}
}
