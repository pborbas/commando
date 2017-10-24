package org.commando.remote.http.receiver;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.ContentType;
import org.commando.exception.DispatchException;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.receiver.CommandReceiver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public abstract class AbstractHttpCommandReceiverServlet extends HttpServlet {

    private static final Log LOGGER = LogFactory.getLog(AbstractHttpCommandReceiverServlet.class);
    private static final long serialVersionUID = 1L;
	private ContentType contentType=ContentType.APPLICATION_JSON;

    private CommandReceiver commandReceiver;

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.process(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
	IOUtils.write(this.commandReceiver.toString(), resp.getOutputStream());

    }

    protected void process(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    	try {
            TextDispatcherCommand textDispatcherCommand = this.parseRequest(req);
			LOGGER.debug("Command received: " + textDispatcherCommand.toString(LOGGER.isDebugEnabled()));
            TextDispatcherResult textDispatcherResult = this.commandReceiver.execute(textDispatcherCommand);
            this.writeResponse(resp, textDispatcherResult);
        } catch (DispatchException e) {
            LOGGER.error(e, e);
        }
    }

    protected void writeResponse(final ServletResponse response, final TextDispatcherResult textDispatcherResult) throws DispatchException {
        try {
            IOUtils.write(textDispatcherResult.getTextResult(), response.getOutputStream(), this.contentType.getCharset().name());
            for (String headerName:textDispatcherResult.getHeaders().keySet()) {
                ((HttpServletResponse)response).setHeader(headerName, textDispatcherResult.getHeader(headerName));
            }
			LOGGER.debug("Response sent:"+textDispatcherResult.toString(LOGGER.isDebugEnabled()));
        } catch (IOException e) {
            throw new DispatchException("Error while writing result to response:" + e, e);
        }
    }

    public TextDispatcherCommand parseRequest(final HttpServletRequest httpRequest) throws DispatchException {
        try {
            String textCommand = IOUtils.toString(httpRequest.getInputStream(), this.contentType.getCharset().name());
            TextDispatcherCommand textDispatcherCommand = new TextDispatcherCommand(textCommand);
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            String headerName;
            while (headerNames.hasMoreElements()) {
                headerName = headerNames.nextElement();
                textDispatcherCommand.setHeader(headerName, httpRequest.getHeader(headerName));
            }
            return textDispatcherCommand;
        } catch (IOException e) {
            throw new DispatchException("Error while convert http request into Command object:" + e, e);
        }
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        this.commandReceiver = this.initCommandReceiver(config);
    }

    protected abstract CommandReceiver initCommandReceiver(final ServletConfig config) throws ServletException;

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
}
