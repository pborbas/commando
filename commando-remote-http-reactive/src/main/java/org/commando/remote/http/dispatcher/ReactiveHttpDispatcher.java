package org.commando.remote.http.dispatcher;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.core.reactive.dispatcher.ReactiveDispatcher;
import org.commando.dispatcher.Dispatcher;
import org.commando.dispatcher.filter.*;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.exception.RemoteExceptionUtil;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Remote reactive HTTP implementation of the {@link Dispatcher}
 */
public class ReactiveHttpDispatcher implements ReactiveDispatcher {

	private static final Log LOGGER = LogFactory.getLog(ReactiveHttpDispatcher.class);

	private final Serializer serializer;
	private final String targetUrl;
	private int timeout = 30000;
	private Charset charset = Charset.defaultCharset();
	private final List<CommandFilter> commandFilters = new LinkedList<>();

	public ReactiveHttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(serializer, targetUrl);
	}

	public ReactiveHttpDispatcher(final Serializer serializer, final String targetUrl) {
		this.serializer = serializer;
		this.targetUrl = targetUrl;
	}

	@Override
	public <C extends Command<R>, R extends Result> Mono<R> dispatch(C command) throws DispatchException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Executing command:" + command.getCommandType() + ". ID:" + command.getCommandId());
		}
		long start = System.currentTimeMillis();
		for (CommandFilter filter:this.commandFilters) {
			command = (C) filter.filter(command);
			LOGGER.debug("Filter applied on command: "+filter.getClass().getName());
		}
		TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(this.serializer.toText(command),
				command.getHeaders());
		String uri = this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
		final C filteredCommand = command;
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
				.responseSingle((resp, bytes) -> {
					TextDispatcherResult textDispatcherResult = toEmptyResultWithHeaders(filteredCommand, resp);
					return bytes.handle((responseBody, sink) -> {
						String textResult = responseBody.readCharSequence(responseBody.readableBytes(), charset)
								.toString();
						String exceptionClassName = textDispatcherResult
								.getHeader(RemoteDispatcher.HEADER_RESULT_EXCEPTION_CLASS);
						if (exceptionClassName != null) {
							sink.error(RemoteExceptionUtil.convertToException(textResult, exceptionClassName));
						} else {
							try {
								R result = (R) this.serializer.toResult(textResult);
								result.getHeaders().putAll(textDispatcherResult.getHeaders());
								String executionTime = new Long(System.currentTimeMillis() - start).toString();
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Finished command:" + filteredCommand.getCommandType() + ". ID:" + filteredCommand.getCommandId()
											+ " (" + executionTime + "msec)");
								}
								sink.next(result);
							} catch (Throwable e) {
								sink.error(new RemoteDispatchException(textResult, e));
							}
						}
					});
				});
	}

	public void addCommandFilter(final CommandFilter filter) {
		this.commandFilters.add(filter);
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