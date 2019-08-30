package com.arrow.kronos.processor;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaAdmin;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.KronosEngineContext;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.pegasus.kafka.KafkaConsumerAbstract;

import moonstone.acs.JsonUtils;

public abstract class KafkaTelemetryProcessorAbstract extends KafkaConsumerAbstract implements CommandLineRunner {

	@Autowired
	private KronosEngineContext context;
	@Autowired
	private Environment env;
	@Autowired
	private KafkaAdmin admin;

	private final String suffix;

	public KafkaTelemetryProcessorAbstract(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public void run(String... args) throws Exception {
		String method = "run";
		String enabled = getEnvProperty("enabled", "true");
		if (enabled.equalsIgnoreCase("true")) {
			start();
		} else {
			logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
		}
	}

	@Override
	public void start() {
		String method = "start";
		try {
			String topic = getTopicPattern().pattern();
			logInfo(method, "checking topic: %s ...", topic);
			AdminClient client = AdminClient.create(admin.getConfig());
			Set<String> existingTopics = client.listTopics().names().get();
			if (existingTopics.contains(topic)) {
				logInfo(method, "topic already exists: %s", topic);
			} else {
				logInfo(method, "creating topic: %s", topic);
				client.createTopics(Collections.singleton(new NewTopic(topic, 3, (short) 1)));
			}
		} catch (Exception e) {
			logError(method, "error checking topic", e);
		}
		super.start();
	}

	@Override
	protected Pattern getTopicPattern() {
		String method = "getTopicPattern";
		String pattern = KronosEngineConstants.KafkaTelemetryProcessor.topic(context.getApplicationEngine().getId(),
				suffix);
		logInfo(method, "pattern: %s", pattern);
		return Pattern.compile(pattern);
	}

	@Override
	protected void process(String topic, String key, String message) {
		String method = "KafkaTelemetryProcessorAbstract.process";
		try {
			doProcessTelemetry(JsonUtils.fromJson(message, TelemetryWrapper.class));
		} catch (Exception e) {
			logError(method, e);
		}
	}

	protected KronosEngineContext getContext() {
		return context;
	}

	protected String getEnvProperty(String property, String defaultValue) {
		return env.getProperty(String.format("%s.%s", getClass().getName(), property), defaultValue).trim();
	}

	@Override
	protected int getNumConsumers() {
		return Integer.parseInt(getEnvProperty("numThreads", "" + KronosEngineConstants.DEFAULT_PROCESSOR_NUM_THREADS));
	}

	protected abstract void doProcessTelemetry(TelemetryWrapper wrapper);
}
