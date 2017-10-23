package org.commando.remote.jms.dispatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commando.command.Command;
import org.commando.exception.DispatchException;
import org.commando.remote.dispatcher.AbstractRemoteDispatcher;
import org.commando.remote.model.TextDispatcherCommand;
import org.commando.remote.model.TextDispatcherResult;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.commando.util.CommandUtil;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Enumeration;

public class JmsDispatcher extends AbstractRemoteDispatcher {

    private static final Log LOGGER = LogFactory.getLog(JmsDispatcher.class);

    private final JmsTemplate commandJmsTemplate;
    private final JmsTemplate resultJmsTemplate;

    public JmsDispatcher(final JmsTemplate commandJmsTemplate, final JmsTemplate resultJmsTemplate, final Serializer serializer) {
        super(serializer);
        this.commandJmsTemplate = commandJmsTemplate;
        this.resultJmsTemplate = resultJmsTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TextDispatcherResult executeRemote(final Command<? extends Result> command, final TextDispatcherCommand commandMessage, final Long timeout) throws DispatchException {
        LOGGER.debug("Dispatching command through JMS. Command ID:" + command.getCommandId());
        try {
            this.commandJmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(final Session session) throws JMSException {
                    TextMessage message = session.createTextMessage();
                    message.setJMSExpiration(timeout);
                    for (String headerKey : commandMessage.getHeaders().keySet()) {
                        message.setStringProperty(headerKey, commandMessage.getHeaders().get(headerKey));
                    }
                    message.setJMSMessageID(command.getCommandId());
                    message.setText(commandMessage.getTextCommand());
                    return message;
                }
            });
            if (CommandUtil.isNoResultCommand(command)) {
                LOGGER.debug("Command sent through JMS. Command ID:" + command.getCommandId() + ". Doesn't wait for result");
                return new TextDispatcherResult(command.getCommandId(), null);
            } else {
                LOGGER.debug("Command sent through JMS. Waiting for result. Command ID:" + command.getCommandId());
                TextMessage jmsResultMessage = this.resultJmsTemplate.receive("JMSCorrelationID='" + command.getCommandId() + "'", timeout);
                LOGGER.debug("Result received. Command ID:" + command.getCommandId());
                String textResult = jmsResultMessage.getText();
                TextDispatcherResult resultMessage = new TextDispatcherResult(command.getCommandId(), textResult);
                Enumeration<String> propertyNames = jmsResultMessage.getPropertyNames();
                while (propertyNames.hasMoreElements()) {
                    String headerName = propertyNames.nextElement();
                    resultMessage.setHeader(headerName, jmsResultMessage.getStringProperty(headerName));
                }
                return resultMessage;
            }
        } catch (JMSException e) {
            throw new DispatchException("Error while dispatching command through JMS:" + e, e);
        }
    }
}
