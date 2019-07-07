package com.arrow.pegasus.kafka;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.service.ProcessorAbstract;

public abstract class KafkaConsumerAbstract extends ProcessorAbstract implements MessageListener<String, String> {

	@Autowired
	private KafkaProperties kafkaProperties;

	private ConcurrentMessageListenerContainer<String, String> container;

	@Override
	public void start() {
		super.start();
		String method = "start";
		try {
			logInfo(method, "kafka hosts: %s",
					kafkaProperties.getBootstrapServers().stream().collect(Collectors.joining(",")));
			DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(
					kafkaProperties.buildConsumerProperties());
			logInfo(method, "creating container, consumers = %d", getNumConsumers());
			container = new ConcurrentMessageListenerContainer<>(factory, new ContainerProperties(getTopicPattern()));
			container.setConcurrency(getNumConsumers());
			container.setupMessageListener(this);
			container.start();
			logInfo(method, "container started");
		} catch (Exception e) {
			logError(method, e);
		}
	}

	@Override
	public void onMessage(ConsumerRecord<String, String> record) {
		String method = "onMessage";
		try {
			logDebug(method, "topic: %s, key: %s, message size: %d", record.topic(), record.key(),
					record.value().length());
			process(record.topic(), record.key(), record.value());
		} catch (Exception e) {
			logError(method, e);
		}
	}

	@Override
	public void preDestroy() {
		String method = "preDestroy";
		super.preDestroy();
		try {
			if (container != null) {
				logInfo(method, "stopping container ...");
				container.stop();
			}
		} catch (Throwable t) {
			logError(method, t);
		}
		logInfo(method, "complete");
	}

	@Override
	protected final int getServiceNumThreads() {
		// no processor threads
		return 0;
	}

	protected int getNumConsumers() {
		return CoreConstant.DEFAULT_KAFKA_CONSUMER_NUM_THREADS;
	}

	protected abstract Pattern getTopicPattern();

	protected abstract void process(String topic, String key, String message);
}
