package org.commando.remote.http.dispatcher;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.AbstractRemoteDispatcher;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.http.model.ReactiveTextDispatcherResult;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

import java.util.Iterator;
import java.util.Map;

/**
 * Remote reactive HTTP implementation of the {@link Dispatcher}
 */
public class ReactiveHttpDispatcher extends AbstractRemoteDispatcher implements RemoteDispatcher {

	private static final Log LOGGER = LogFactory.getLog(ReactiveHttpDispatcher.class);

	private final String targetUrl;

	public ReactiveHttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(serializer, targetUrl);
	}

	public ReactiveHttpDispatcher(final Serializer serializer, final String targetUrl) {
		super(serializer);
		this.targetUrl = targetUrl;
	}

	@Override
	protected TextDispatcherResult executeRemote(final Command<? extends Result> command,
			final TextDispatcherCommand textDispatcherCommand, final Long timeout) throws DispatchException {
		String uri = this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
		LOGGER.debug("Sending HTTP request of command: " + command.getCommandId() + " to:" + uri.toString());
		try {
			TextDispatcherResult textDispatcherResult = new TextDispatcherResult(command.getCommandId(), null);
			String responseBody = HttpClient.create() //
					.tcpConfiguration( //
							tcpClient -> tcpClient //
									.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout.intValue())) //
					//									.option(ChannelOption.SO_TIMEOUT, timeout.intValue())) //
					.headers(headers -> {
						for (String headerKey : textDispatcherCommand.getHeaders().keySet()) {
							headers.set(headerKey, textDispatcherCommand.getHeaders().get(headerKey));
						}
						headers.set(HttpHeaderNames.CONTENT_TYPE, serializer.getContentType());
					}) //
					.post().uri(uri) //

					.send(ByteBufFlux.fromString(Mono.just(textDispatcherCommand.getTextCommand())))
					.response()
					.map()
//					.responseContent()   // Receives the response body.
//					.aggregate().asString().block();

					.responseSingle((resp, bytes) -> {
						for (Iterator<Map.Entry<String, String>> it = resp.responseHeaders().iteratorAsString(); it
								.hasNext(); ) {
							Map.Entry<String, String> header = it.next();
							textDispatcherResult.setHeader(header.getKey(), header.getValue());
						}
						return Mono.just(textDispatcherResult);
					}).block();

			return new TextDispatcherResult(command.getCommandId(), responseBody);
		} catch (Exception e) {
			throw new RemoteDispatchException(
					"Error while sending command:" + command.getCommandId() + " through HTTP to URL:" + this.targetUrl
							+ ". Error message:" + e, e);
		}
	}

	public String getTargetUrl() {
		return targetUrl;
	}

}
