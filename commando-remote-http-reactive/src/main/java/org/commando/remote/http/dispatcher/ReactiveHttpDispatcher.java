package org.commando.remote.http.dispatcher;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.core.reactive.dispatcher.AbstractReactiveDispatcher;
import org.commando.core.reactive.dispatcher.ReactiveExecutor;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.exception.RemoteExceptionUtil;
import org.commando.remote.http.exception.*;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * Remote reactive HTTP implementation of the {@link Dispatcher}
 */
public class ReactiveHttpDispatcher extends AbstractReactiveDispatcher implements ReactiveExecutor {

	private static final Log LOGGER = LogFactory.getLog(ReactiveHttpDispatcher.class);

	private final Serializer serializer;
	private final String targetUrl;
	private int timeout = 30000;
	private Charset charset = Charset.defaultCharset();

	public ReactiveHttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(serializer, targetUrl);
	}

	public ReactiveHttpDispatcher(final Serializer serializer, final String targetUrl) {
		this.serializer = serializer;
		this.targetUrl = targetUrl;
	}

	@Override
	protected ReactiveExecutor getExecutor() {
		return this;
	}

	@Override
	public <C extends Command<R>, R extends Result> Mono<R> execute(C command) {
		try {
			TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(this.serializer.toText(command),
					command.getHeaders());
			String uri = this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
			//TODO: reactive: let specify the executorservice
			return HttpClient.create() //
					.tcpConfiguration( //
							tcpClient -> tcpClient //
									.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)) //
					//									.option(ChannelOption.SO_TIMEOUT, timeout.intValue())) //
					.doOnResponseError((response, t) -> {
						LOGGER.error("Error HTTP response: " + response + ". Message: " + t.getMessage(), t);
					}).doOnRequestError((request, t) -> {
						LOGGER.error("Error HTTP request: " + request + ". Message: " + t.getMessage(), t);
					}).headers(headers -> {
						for (String headerKey : textDispatcherCommand.getHeaders().keySet()) {
							headers.set(headerKey, textDispatcherCommand.getHeaders().get(headerKey));
						}
						headers.set(HttpHeaderNames.CONTENT_TYPE, serializer.getContentType());
					}) //
					.post().uri(uri) //
					.send(ByteBufFlux.fromString(Mono.just(textDispatcherCommand.getTextCommand())))
					.responseSingle((resp, bytes) -> {
						TextDispatcherResult textDispatcherResult = toEmptyResultWithHeaders(command, resp);
						if (HttpResponseStatus.OK.equals(resp.status())) {
							return bytes.handle((responseBody, sink) -> {
								String textResult = responseBody.readCharSequence(responseBody.readableBytes(), charset)
										.toString();
								String exceptionClassName = textDispatcherResult
										.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS);
								if (exceptionClassName != null) {
									sink.error(RemoteExceptionUtil.convertToException(textResult, exceptionClassName));
								} else {
									getMetrics().error(command);
									try {
										R result = (R) this.serializer.toResult(textResult);
										result.getHeaders().putAll(textDispatcherResult.getHeaders());
										sink.next(result);
									} catch (Throwable e) {
										sink.error(new RemoteDispatchException(textResult, e));
									}
								}
							});
						} else if (resp.status().code() == 301 || resp.status().code() == 302) {
							return Mono.error(new RemoteDispatchException(
									"Unexpected redirect. Status: " + resp.status() + " with location: "
											+ textDispatcherResult.getHeader("Location")));
						} else if (resp.status().code() == 400) {
							return Mono.error(new BadRequestException(
									"Status: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						} else if (resp.status().code() == 401) {
							return Mono.error(new UnauthenticatedException(
									"Status: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						} else if (resp.status().code() == 403) {
							return Mono.error(new UnauthorizedException(
									"Status: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						} else if (resp.status().code() == 404) {
							return Mono.error(new NotFoundException(
									"Status: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						} else if (resp.status().code() == 409) {
							return Mono.error(new ConflictException(
									"Status: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						} else {
							return Mono.error(new RemoteDispatchException(
									"Incorrect response code: " + resp.status() + " with body: " + textDispatcherResult
											.getTextResult()));
						}
					});
		} catch (CommandSerializationException e) {
			return Mono.error(e);
		} catch (Throwable t) {
			return Mono.error(t);
		}
	}

	private <C extends Command<R>, R extends Result> TextDispatcherResult toEmptyResultWithHeaders(C command,
			HttpClientResponse resp) {
		TextDispatcherResult textDispatcherResult = new TextDispatcherResult(command.getCommandId(), null);
		for (Iterator<Map.Entry<String, String>> it = resp.responseHeaders().iteratorAsString(); it.hasNext(); ) {
			Map.Entry<String, String> header = it.next();
			textDispatcherResult.setHeader(header.getKey(), header.getValue());
		}
		return textDispatcherResult;
	}

	public ReactiveHttpDispatcher setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

}