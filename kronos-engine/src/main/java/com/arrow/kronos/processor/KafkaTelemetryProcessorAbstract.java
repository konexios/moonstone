package com.arrow.kronos.processor;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import com.arrow.acs.JsonUtils;
import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.KronosEngineContext;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.pegasus.kafka.KafkaConsumerAbstract;

public abstract class KafkaTelemetryProcessorAbstract extends KafkaConsumerAbstract implements CommandLineRunner {

    @Autowired
    private KronosEngineContext context;
    @Autowired
    private Environment env;

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
