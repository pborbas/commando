package org.commando.remote.http.dispatcher;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.core.reactive.dispatcher.ReactiveDispatcher;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.exception.DispatchException;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

/**
 * Remote reactive HTTP implementation of the {@link Dispatcher}
 */
public class ReactiveHttpDispatcher implements ReactiveDispatcher {

	private static final Log LOGGER = LogFactory.getLog(ReactiveHttpDispatcher.class);

	private final Serializer serializer;
	private final String targetUrl;
	private int timeout = 30000;

	public ReactiveHttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(serializer, targetUrl);
	}

	public ReactiveHttpDispatcher(final Serializer serializer, final String targetUrl) {
		this.serializer = serializer;
		this.targetUrl = targetUrl;
	}

	@Override
	public <C extends Command<R>, R extends Result> Mono<R> dispatch(C command) throws DispatchException {
		TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(this.serializer.toText(command),
				command.getHeaders());
		String uri = this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
		LOGGER.debug("Sending HTTP request of command: " + command.getCommandId() + " to:" + uri.toString());
		try {

			//TODO: reactive: let specify the executorservice
			return HttpClient.create() //
					.tcpConfiguration( //
							tcpClient -> tcpClient //
									.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)) //
					//									.option(ChannelOption.SO_TIMEOUT, timeout.intValue())) //
					.headers(headers -> {
						for (String headerKey : textDispatcherCommand.getHeaders().keySet()) {
							headers.set(headerKey, textDispatcherCommand.getHeaders().get(headerKey));
						}
						headers.set(HttpHeaderNames.CONTENT_TYPE, serializer.getContentType());
					}) //
					.post().uri(uri) //
					.send(ByteBufFlux.fromString(Mono.just(textDispatcherCommand.getTextCommand())))
					.responseContent()   // Receives the response body.
					.aggregate().asString().handle((responseBody, sink) -> {
						try {
							R result = (R) this.serializer.toResult(responseBody);
							//TODO: reactive: deal with headers
							// result.getHeaders().putAll(textDispatcherResult.getHeaders());
							//TODO: reactive: deal with RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS
							sink.next(result);
						} catch (CommandSerializationException e) {
							sink.error(e);
						}

					});



			//					.responseSingle((resp, bytes) -> {
			//						for (Iterator<Map.Entry<String, String>> it = resp.responseHeaders().iteratorAsString(); it
			//								.hasNext(); ) {
			//							Map.Entry<String, String> header = it.next();
			//							textDispatcherResult.setHeader(header.getKey(), header.getValue());
			//						}
			//						return Mono.just(textDispatcherResult);
			//					}).block();
		} catch (Exception e) {
			throw new RemoteDispatchException(
					"Error while sending command:" + command.getCommandId() + " through HTTP to URL:" + this.targetUrl
							+ ". Error message:" + e, e);
		}
	}

}
