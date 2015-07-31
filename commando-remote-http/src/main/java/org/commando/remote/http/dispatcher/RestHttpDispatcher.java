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
import org.apache.http.impl.client.HttpClients;
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

import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Remote HTTP implementation of the {@link Dispatcher}
 */
public class RestHttpDispatcher extends AbstractRemoteDispatcher implements RemoteDispatcher {

	private static final Log LOGGER = LogFactory.getLog(RestHttpDispatcher.class);

	private final CloseableHttpClient httpclient;
	private final String targetUrl;
	private ContentType contentType=ContentType.APPLICATION_JSON;

	public RestHttpDispatcher(final String targetUrl, final Serializer serializer) {
		this(HttpClients.createDefault(), serializer, targetUrl);
	}

	public RestHttpDispatcher(final CloseableHttpClient httpclient, final Serializer serializer,
			final String targetUrl) {
		super(serializer);
		this.httpclient = httpclient;
		this.targetUrl = targetUrl;
	}

	@Override
	protected TextDispatcherResult executeRemote(final Command<? extends Result> command,
			final TextDispatcherCommand textDispatcherCommand, final Long timeout) throws DispatchException {
		try {
			HttpRequestBase httpRequest = this.createRequest(command, textDispatcherCommand, timeout);
			LOGGER.debug("Sending HTTP request of command: " + command.getCommandId() + " to:" + httpRequest.getURI()
					.toString() + " Method:" + httpRequest.getMethod());
			CloseableHttpResponse response = this.httpclient.execute(httpRequest);
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

	protected String createHttpPath(final Command<? extends Result> command) throws DispatchException {
		Path pathAnnotation = command.getClass().getAnnotation(Path.class);
		if (pathAnnotation != null) {
			String pathTemplate = pathAnnotation.value();
			if (pathTemplate.contains("{")) {
				try {
					for (Field field : command.getClass().getDeclaredFields()) {
						if (pathTemplate.contains("{" + field.getName() + "}")) {
							field.setAccessible(true);
							pathTemplate = pathTemplate
									.replaceAll("\\{" + field.getName() + "}", field.get(command).toString());
							field.setAccessible(false);
						}
					}
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					throw new DispatchException("Error while resolving http request path:" + e, e);
				}
			}
			return this.targetUrl + pathTemplate;
		} else {
			return this.targetUrl + "/" + command.getClass().getName().replaceAll("\\.", "/");
		}
	}

	protected TextDispatcherResult parseResponse(final CloseableHttpResponse httpResponse,
			final Command<? extends Result> command) throws DispatchException {
		int status = httpResponse.getStatusLine().getStatusCode();
		HttpEntity entity = httpResponse.getEntity();
		String responseBody;
		try {
			responseBody = EntityUtils.toString(entity, this.contentType.getCharset());
			TextDispatcherResult textDispatcherResult = new TextDispatcherResult(command.getCommandId(), responseBody);
			for (Header header : httpResponse.getAllHeaders()) {
				textDispatcherResult.setHeader(header.getName(), header.getValue());
			}
			return textDispatcherResult;
		} catch (ParseException | IOException e) {
			throw new RemoteDispatchException("Error reading HTTP response. HTTP status:" + status + "." + e, e);
		}
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

}
