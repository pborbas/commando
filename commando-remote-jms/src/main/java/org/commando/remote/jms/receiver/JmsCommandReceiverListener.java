package org.commando.remote.jms.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.dispatcher.Dispatcher;
import org.commando.exception.CommandSerializationException;
import org.commando.exception.DispatchException;
import org.commando.remote.jms.dispatch.JmsDispatcher;
import org.commando.remote.jms.dispatch.JmsTemplate;
import org.commando.remote.jms.dispatch.MessageCreator;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.receiver.CommandReceiver;

import javax.jms.*;
import java.util.Enumeration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Configure this as a message listener for the queue where you send commands
 * with {@link JmsDispatcher}
 * 
 * @author borbasp
 */
public class JmsCommandReceiverListener implements MessageListener {
    private static final Log LOGGER = LogFactory.getLog(JmsCommandReceiverListener.class);

    private final CommandReceiver commandReceiver;
    private final JmsTemplate resultJmsTemplate;

    public JmsCommandReceiverListener(final CommandReceiver commandReceiver, final JmsTemplate resultJmsTemplate) {
        super();
        this.commandReceiver = commandReceiver;
        this.resultJmsTemplate = resultJmsTemplate;
        LOGGER.info("JMS command receiver initialized");
    }

    @Override
    public void onMessage(final Message message) {
        try {
            TextDispatcherCommand commandMessage = this.parseReceivedMessage(message);
			CompletableFuture<TextDispatcherResult> textDispatcherResultCompletableFuture = this.commandReceiver.execute(commandMessage);
			textDispatcherResultCompletableFuture.thenApply(result -> {
				try {
					this.sendResult(result, this.getResultTimeout(commandMessage));
				} catch (Exception e) {
					throw new CompletionException(e);
				}
				return result;
			});
        } catch (DispatchException e) {
            LOGGER.error("Error while dispatchig message received through JMS: " + e, e);
        }
    }

    protected long getResultTimeout(final TextDispatcherCommand dispatchCommand) {
        String timeoutHeader = dispatchCommand.getHeader(Dispatcher.HEADER_TIMEOUT);
        Long requestTimeout=(timeoutHeader != null) ? Long.valueOf(timeoutHeader) : null;
        if (requestTimeout!=null && requestTimeout<this.commandReceiver.getTimeout()) {
            return requestTimeout;
        } 
        return this.commandReceiver.getTimeout();
    }

    @SuppressWarnings("unchecked")
    private TextDispatcherCommand parseReceivedMessage(final Message message) throws DispatchException {
        if (message instanceof TextMessage) {
            try {
                TextMessage textMessage = (TextMessage) message;
                String textCommand = textMessage.getText();
                TextDispatcherCommand commandMessage = new TextDispatcherCommand(textCommand);
                Enumeration<String> propertyNames = textMessage.getPropertyNames();
                String headerName;
                while (propertyNames.hasMoreElements()) {
                    headerName = propertyNames.nextElement();
                    commandMessage.setHeader(headerName, textMessage.getStringProperty(headerName));
                }
                return commandMessage;
            } catch (JMSException e) {
                throw new DispatchException("Error while parsing JMS message:" + e, e);
            }
        } else {
            throw new DispatchException("Invalid message type. Only TextMessage is supported");
        }

    }

    private void sendResult(final TextDispatcherResult resultMessage, final long timeout) throws JMSException, CommandSerializationException {
        LOGGER.debug("Command dispatched. Sending to result queue.");
        this.resultJmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(final Session session) throws JMSException {
                TextMessage msg = session.createTextMessage();
                msg.setText(resultMessage.getTextResult());
                msg.setJMSTimestamp(System.currentTimeMillis());
                msg.setJMSCorrelationID(resultMessage.getCommandId());
                msg.setJMSExpiration(timeout);
                for (String headerName : resultMessage.getHeaders().keySet()) {
                    msg.setStringProperty(headerName, resultMessage.getHeader(headerName));
                }
                return msg;
            }
        });
    }

}
