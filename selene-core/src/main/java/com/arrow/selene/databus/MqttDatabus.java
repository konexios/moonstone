package com.arrow.selene.databus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.arrow.acn.client.cloud.CustomMqttClient;
import com.arrow.acn.client.cloud.MessageListener;
import com.arrow.selene.SeleneProperties;
import com.arrow.selene.service.ConfigService;
import com.arrow.selene.service.CryptoService;

public class MqttDatabus extends DatabusAbstract implements MessageListener {
	private CustomMqttClient mqttClient;
	private AtomicReference<Set<String>> queuesToSubscribe = new AtomicReference<>(new HashSet<>());

	public MqttDatabus() {
		logInfo(getClass().getSimpleName(), "...");

		SeleneProperties seleneProperties = ConfigService.getInstance().getSeleneProperties();
		Validate.notEmpty(seleneProperties.getMqttDatabusUrl(), "mqttDatabusUrl is not defined");
		mqttClient = new CustomMqttClient(seleneProperties.getMqttDatabusUrl());
		String username = seleneProperties.getMqttDatabusUsername();
		if (StringUtils.isNotEmpty(username)) {
			mqttClient.getOptions().setUserName(CryptoService.getInstance().decrypt(username));
		}
		String password = seleneProperties.getMqttDatabusPassword();
		if (StringUtils.isNotEmpty(password)) {
			mqttClient.getOptions().setPassword(CryptoService.getInstance().decrypt(password).toCharArray());
		}
		mqttClient.setListener(this);
	}

	@Override
	public void processMessage(String topic, byte[] payload) {
		notifyListener(topic, payload);
	}

	@Override
	public void start() {
		String method = "start";
		Validate.isTrue(!isStopped(), "databus is already stopped");
		Set<String> queues = queuesToSubscribe.getAndSet(null);
		if (queues != null) {
			mqttClient.connect(false);
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			String[] topics = queues.toArray(new String[queues.size()]);
			if (topics != null && topics.length > 0) {
				mqttClient.subscribe(topics);
			}
		}
		logInfo(method, "databus started");
	}

	@Override
	public void stop() {
		String method = "stop";
		setStopped(true);
		mqttClient.disconnect();
		logInfo(method, "databus stopped");
	}

	@Override
	public void send(String queue, byte[] message) {
		String method = "send";
		if (isStopped()) {
			return;
		}
		logDebug(method, "publishing to queue: %s, message size: %d", queue, message == null ? 0 : message.length);
		mqttClient.publish(queue, message, 2);
	}

	@Override
	protected void checkAndCreateQueue(String queue) {
		if (queuesToSubscribe.get() != null) {
			queuesToSubscribe.get().add(queue);
		} else {
			mqttClient.subscribe(queue);
			String[] topics = mqttClient.getTopics();
			if (topics == null) {
				topics = new String[0];
			}
			topics = Arrays.copyOf(topics, topics.length + 1);
			topics[topics.length - 1] = queue;
			mqttClient.setTopics(topics);
		}
	}
}
