package com.arrow.pegasus.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PreDestroy;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import moonstone.acs.AcsLogicalException;

public abstract class RabbitListenerAbstract extends QueueListenerAbstract
        implements MessageListener, ApplicationListener<ListenerContainerConsumerFailedEvent> {

	private final static int DEFAULT_CONCURRENT_CONSUMERS = 1;
	private final static int DEFAULT_PREFETCH_COUNT = 1;

	@Autowired
	private ConnectionFactory connectionFactory;

	private RabbitAdmin rabbitAdmin;
	private SimpleMessageListenerContainer container;
	private ApplicationEventPublisher applicationEventPublisher;
	private final Map<SimpleMessageListenerContainer, AtomicBoolean> shuttingDown = new ConcurrentHashMap<>();

	@Override
	protected void postConstruct() {
		super.postConstruct();
		rabbitAdmin = new RabbitAdmin(connectionFactory);
	}

	@Override
	public void start() {
		super.start();
		String method = "RabbitListenerAbstract.start";
		if (!isStarted()) {
			for (String queue : getQueues()) {
				if (rabbitAdmin.getQueueProperties(queue) == null) {
					logInfo(method, "declaring queue %s", queue);
					// rabbitAdmin.declareQueue(new Queue(queue, true));
					throw new AcsLogicalException("Queue does not exist: " + queue);
				}
			}

			container = new SimpleMessageListenerContainer(connectionFactory);
			container.addQueueNames(getQueues());
			container.setMessageListener(this);
			container.setApplicationEventPublisher(applicationEventPublisher);
			container.setConcurrentConsumers(getConcurrentConsumers());
			container.setPrefetchCount(getPrefetchCount());
			container.afterPropertiesSet();
			container.start();
			setStarted(true);
			logInfo(method, "started");
		} else {
			logInfo(method, "already started");
		}
	}

	@PreDestroy
	public void preDestroy() {
		super.preDestroy();
		String method = "RabbitListenerAbstract.preDestroy";
		if (container != null) {
			try {
				logInfo(method, "stop container ...");
				container.destroy();
			} catch (Throwable t) {
			}
			container = null;
		}
		setStarted(false);
		logInfo(method, "stopped");
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public void onMessage(Message message) {
		if (message != null) {
			receiveMessage(message.getBody(), message.getMessageProperties().getReceivedRoutingKey());
		}
	}

	protected RabbitAdmin getRabbitAdmin() {
		return rabbitAdmin;
	}

	protected void onMessageListenerContainerRestart() {
	}

	protected int getConcurrentConsumers() {
		return DEFAULT_CONCURRENT_CONSUMERS;
	}

	protected int getPrefetchCount() {
		return DEFAULT_PREFETCH_COUNT;
	}

	@Override
	public void onApplicationEvent(ListenerContainerConsumerFailedEvent event) {
		String method = "onApplicationEvent";
		logDebug(method, "event: %s", event);
		if (event.getSource() == container && event.isFatal()) {
			AtomicBoolean shutdownFlag = shuttingDown.computeIfAbsent(container, c -> new AtomicBoolean());
			if (shutdownFlag.compareAndSet(false, true)) {
				Executors.newSingleThreadExecutor().execute(() -> {
					while (container.isActive()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
					container.removeQueueNames(getQueues());

					logDebug(method, "onMessageListenerContainerRestart...");
					onMessageListenerContainerRestart();

					logDebug(method, "start container...");
					container.addQueueNames(getQueues());
					container.afterPropertiesSet();
					container.start();
					shuttingDown.remove(container);
					logDebug(method, "container restarted");
				});
			}
		}
	}

}
