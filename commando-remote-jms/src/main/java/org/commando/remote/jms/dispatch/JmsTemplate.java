package org.commando.remote.jms.dispatch;

import javax.jms.*;
import java.util.concurrent.locks.ReentrantLock;

public class JmsTemplate {
	private final ConnectionFactory connectionFactory;
	private final Destination destination;
	private final ReentrantLock lock = new ReentrantLock();
	private boolean transacted = false;
	private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
	private ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
	private ThreadLocal<MessageProducer> producerThreadLocal = new ThreadLocal<>();
	private int deliveryMode = DeliveryMode.NON_PERSISTENT;
	private int priority = 1;
	private long timeToLive = 180000;

	public JmsTemplate(final ConnectionFactory connectionFactory, final Destination destination) {
		super();
		this.connectionFactory = connectionFactory;
		this.destination = destination;
	}

	public void send(final MessageCreator messageCreator) throws JMSException {
		Connection connection = createConnection();
		try {
			Session session = this.createSession(connection);
			MessageProducer producer = createProducer(session);
			Message message = messageCreator.createMessage(session);
			producer.send(message, deliveryMode, priority, timeToLive);
		} catch (JMSException e) {
			closeConnection(connection);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Message> T receive(final String messageSelector, final long timeout) throws JMSException {
		Connection connection = createConnection();
		try {
			Session session = createSession(connection);
			MessageConsumer consumer = session.createConsumer(destination, messageSelector);
			T resultMessage = (T) consumer.receive(timeout);
			return resultMessage;
		} catch (JMSException e) {
			closeConnection(connection);
			throw e;
		}
	}

	protected Session createSession(final Connection connection) throws JMSException {
		Session session = sessionThreadLocal.get();
		if (session == null) {
			session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
			sessionThreadLocal.set(session);
		}
		return session;
	}

	protected Connection createConnection() throws JMSException {
		Connection connection = connectionThreadLocal.get();
		if (connection == null) {
			connection = connectionFactory.createConnection();
			connection.start();
			connectionThreadLocal.set(connection);
		}
		return connection;
	}

	protected MessageProducer createProducer(Session session) throws JMSException {
		MessageProducer producer = producerThreadLocal.get();
		if (producer == null) {
			producer = session.createProducer(destination);
			producerThreadLocal.set(producer);
		}
		return producer;
	}

	protected void closeConnection(final Connection connection) throws JMSException {
		connection.close();
	}

	public JmsTemplate setTransacted(boolean transacted) {
		this.transacted = transacted;
		return this;
	}

	public JmsTemplate setDeliveryMode(int deliveryMode) {
		this.deliveryMode = deliveryMode;
		return this;
	}

	public JmsTemplate setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public JmsTemplate setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
		return this;
	}
}
