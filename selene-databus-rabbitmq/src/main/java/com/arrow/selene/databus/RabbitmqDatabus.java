package com.arrow.selene.databus;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.SeleneException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitmqDatabus extends DatabusAbstract {
	private Connection connection;
	private Channel channel;

	public RabbitmqDatabus() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@Override
	public void start() {
		String method = "start";
		Validate.isTrue(!isStopped(), "databus is already stopped");
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			if (queueToListeners.isEmpty()) {
				logWarn(method, "consumer is not started since there's no listener registered");
			} else {
				Set<String> queues = queueToListeners.keySet();
				for (String queue : queues) {
					Consumer consumer = new DefaultConsumer(channel) {
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
								byte[] body) throws IOException {
							String method = "handleDelivery";
							try {
								notifyListener(envelope.getRoutingKey(), body);
							} catch (Exception e) {
								logError(method, "error notifying listener", e);
							} finally {
								channel.basicAck(envelope.getDeliveryTag(), false);
							}
						}
					};
					channel.queueDeclare(queue, true, false, false, null);
					logDebug(method, "created queue: %s", queue);
					channel.basicConsume(queue, false, consumer);
					logDebug(method, "created consumer for queue: %s", queue);
				}
			}
		} catch (Exception e) {
			throw new SeleneException("unable to start databus", e);
		}
		logInfo(method, "started!");
	}

	@Override
	public void stop() {
		String method = "stop";
		if (channel != null) {
			logDebug(method, "closing channel ...");
			try {
				channel.close();
			} catch (Throwable ignored) {
			}
			channel = null;
		}
		if (connection != null) {
			logDebug(method, "closing connection ...");
			try {
				connection.close();
			} catch (Throwable ignored) {
			}
			connection = null;
		}
	}

	@Override
	public void send(String queue, byte[] message) {
		String method = "send";
		if (channel == null || isStopped()) {
			return;
		}
		try {
			logDebug(method, "publishing to queue: %s, message size: %d", queue, message == null ? 0 : message.length);
			channel.basicPublish("", queue, null, message);
		} catch (IOException e) {
			logError(method, e);
		}
	}
}
