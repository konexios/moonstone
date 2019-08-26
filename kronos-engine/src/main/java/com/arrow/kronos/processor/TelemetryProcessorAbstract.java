package com.arrow.kronos.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.KronosEngineContext;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Telemetry;
import com.arrow.pegasus.service.RabbitListenerAbstract;

import moonstone.acs.JsonUtils;

public abstract class TelemetryProcessorAbstract extends RabbitListenerAbstract implements CommandLineRunner {
    @Autowired
    private KronosEngineContext context;
    @Autowired
    private Environment env;
    private String queueNamePrefix;

    public TelemetryProcessorAbstract(String queueNamePrefix) {
        super();
        this.queueNamePrefix = queueNamePrefix;
    }

    @Override
    protected void postConstruct() {
        super.postConstruct();
        String method = "postConstruct";
        logDebug(method, "declaring exchange %s", KronosEngineConstants.ProcessorQueue.EXCHANGE);
        DirectExchange exchange = new DirectExchange(KronosEngineConstants.ProcessorQueue.EXCHANGE);
        getRabbitAdmin().declareExchange(exchange);

        String queueName = KronosEngineConstants.ProcessorQueue.queueName(queueNamePrefix,
                getContext().getApplicationEngine().getId());
        logDebug(method, "declaring queue %s", queueName);
        Queue queue = new Queue(queueName, true);
        getRabbitAdmin().declareQueue(queue);

        logInfo(method, "binding queue %s: ", queueName);
        getRabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(exchange).with(queue.getName()));

        setQueues(new String[] { queueName });
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
    protected int getNumWorkerThreads() {
        return Integer.parseInt(getEnvProperty("numThreads", "" + KronosEngineConstants.DEFAULT_PROCESSOR_NUM_THREADS));
    }

    @Override
    protected int getConcurrentConsumers() {
        return Integer.parseInt(
                getEnvProperty("concurrentConsumers", "" + KronosEngineConstants.DEFAULT_CONCURRENT_CONSUMERS));
    }

    @Override
    protected int getPrefetchCount() {
        return Integer.parseInt(getEnvProperty("prefetchCount", "" + KronosEngineConstants.DEFAULT_PREFETCH_COUNT));
    }

    @Override
    public void receiveMessage(byte[] message, String queue) {
        String method = "receiveMessage";
        logDebug(method, "queue: %s, size: %d", queue, message.length);
        try {
            TelemetryWrapper wrapper = null;
            try {
                wrapper = JsonUtils.fromJsonBytes(message, TelemetryWrapper.class);
            } catch (Exception e) {
            }
            // backward compatible
            if (wrapper == null || wrapper.getItems() == null || wrapper.getItems().isEmpty()) {
                logWarn(method, "wrapper is null or empty");
                Telemetry telemetry = JsonUtils.fromJsonBytes(message, Telemetry.class);
                if (StringUtils.isEmpty(telemetry.getId())) {
                    logError(method, "**** ERROR: telemetry was not persisted!");
                    return;
                }
                wrapper = new TelemetryWrapper();
                wrapper.setId(telemetry.getId());
                wrapper.setApplicationId(telemetry.getApplicationId());
                wrapper.setDeviceId(telemetry.getDeviceId());
                wrapper.setTimestamp(telemetry.getTimestamp());
                wrapper.setPayloadSize(telemetry.getPayloadSize());
                wrapper.setItems(getContext().getTelemetryItemService().getTelemetryItemRepository()
                        .findByTelemetryId(telemetry.getId()));
            }
            logDebug(method, "wrapperId: %s, deviceId: %s, items: %d", wrapper.getId(), wrapper.getDeviceId(),
                    wrapper.getItems() == null ? 0 : wrapper.getItems().size());
            final TelemetryWrapper finalWrapper = wrapper;
            blockDispatch(new Runnable() {
                @Override
                public void run() {
                    try {
                        doProcessTelemetry(finalWrapper);
                    } catch (Throwable t) {
                        logError(method, t);
                    }
                }
            });
        } catch (Throwable t) {
            logError(method, t);
        }
    }

    protected KronosEngineContext getContext() {
        return context;
    }

    protected String getEnvProperty(String property, String defaultValue) {
        return env.getProperty(String.format("%s.%s", getClass().getName(), property), defaultValue).trim();
    }

    protected abstract void doProcessTelemetry(TelemetryWrapper wrapper);
}
