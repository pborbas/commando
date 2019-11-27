package org.commando.remote.http.dispatcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.commando.command.Command;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.AbstractRemoteDispatcher;
import org.commando.remote.dispatcher.RemoteDispatcher;
import org.commando.remote.exception.RemoteDispatchException;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 * Serializes the command and sends it over HTTP POST to the configured targetUrl
 */
public class HttpDispatcher extends AbstractRemoteDispatcher implements RemoteDispatcher {

	private static final Log LOGGER = LogFactory.getLog(HttpDispatcher.class);
	public static final int DEFAULT_THREAD_POOL_SIZE = 100;

	private final CloseableHttpClient httpClient;
	private final String targetUrl;

	public HttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(null, serializer, targetUrl, DEFAULT_THREAD_POOL_SIZE);
	}

	public HttpDispatcher(final String targetUrl, final Serializer serializer, int threadPoolSize) {
		this(null, serializer, targetUrl, threadPoolSize);
	}

	public HttpDispatcher(final CloseableHttpClient httpClient, final Serializer serializer, final String targetUrl,
			int threadPoolSize) {
		super(serializer);
		if (httpClient == null) {
			this.httpClient = createDefaultMultiThreadedClient(threadPoolSize);
		} else {
			this.httpClient = httpClient;
		}
		this.targetUrl = targetUrl;
		this.setExecutorService(Executors.newFixedThreadPool(threadPoolSize));
	}

	private CloseableHttpClient createDefaultMultiThreadedClient(int threadPoolSize) {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(threadPoolSize);
		connManager.setDefaultMaxPerRoute(threadPoolSize);
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connManager);
		return httpClientBuilder.build();
	}

	@Override
	protected TextDispatcherResult executeRemote(final Command<? extends Result> command,
			final TextDispatcherCommand textDispatcherCommand, final Long timeout) throws DispatchException {
		try {
			HttpRequestBase httpRequest = this.createRequest(command, textDispatcherCommand, timeout);
			LOGGER.debug("Sending HTTP request of command: " + command.getCommandId() + " to:" + httpRequest.getURI()
					.toString() + " Method:" + httpRequest.getMethod());
			CloseableHttpResponse response = httpClient.execute(httpRequest);
			try {
				return this.parseResponse(response, command);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RemoteDispatchException(
					"Error while sending command:" + command.getCommandId() + " through HTTP to URL:" + this.targetUrl
							+ ". Error message:" + e, e);
		}
	}

	protected HttpRequestBase createRequest(final Command<? extends Result> command,
			final TextDispatcherCommand commandMessage, final Long timeout) throws DispatchException {
		HttpRequestBase request;
		String requestPath = this.createHttpPath(command);
		request = new HttpPost(requestPath);
		ContentType contentType = ContentType.create(this.serializer.getContentType(), this.serializer.getCharset());
		request.setHeader(HttpHeaders.CONTENT_TYPE, contentType.toString());
		((HttpPost) request).setEntity(new StringEntity(commandMessage.getTextCommand(), contentType));
		for (String headerKey : commandMessage.getHeaders().keySet()) {
			request.setHeader(headerKey, commandMessage.getHeaders().get(headerKey));
		}
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout.intValue())
				.setConnectTimeout(timeout.intValue()).build();
		request.setConfig(requestConfig);
		return request;
	}

	protected String createHttpPath(final Command<? extends Result> command) {
		return this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
	}

	protected TextDispatcherResult parseResponse(final CloseableHttpResponse httpResponse,
			final Command<? extends Result> command) throws DispatchException {
		int status = httpResponse.getStatusLine().getStatusCode();
		HttpEntity entity = httpResponse.getEntity();
		String responseBody;
		try {
			responseBody = EntityUtils.toString(entity, this.serializer.getCharset());
			TextDispatcherResult textDispatcherResult = new TextDispatcherResult(command.getCommandId(), responseBody);
			for (Header header : httpResponse.getAllHeaders()) {
				textDispatcherResult.setHeader(header.getName(), header.getValue());
			}
			return textDispatcherResult;
		} catch (ParseException | IOException e) {
			throw new RemoteDispatchException("Error reading HTTP response. HTTP status:" + status + "." + e, e);
		}
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

}
