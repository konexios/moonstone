package com.arrow.kronos;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.service.RabbitListenerAbstract;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.MqttConstants;
import moonstone.acs.JsonUtils;

public class MqttTelemetryListener extends RabbitListenerAbstract implements CommandLineRunner, ContextListener {
    private static final TypeReference<Map<String, String>> MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
    };
    private static final TypeReference<List<Map<String, String>>> LIST_MAP_TYPE_REF = new TypeReference<List<Map<String, String>>>() {
    };

    @Autowired
    private KronosEngineContext context;
    @Autowired
    private KronosApplicationService kronosApplicationService;

    private TopicExchange defaultExchange;
    private DirectExchange appTelemetryExchange;

    @Value("${MqttTelemetryListener.enabled:true}")
    private boolean enabled;
    @Value("${MqttTelemetryListener.numThreads:10}")
    private int numThreads;
    @Value("${MqttTelemetryListener.concurrentConsumers:5}")
    private int concurrentConsumers;
    @Value("${MqttTelemetryListener.prefetchCount:10}")
    private int prefetchCount;

    @Override
    public void run(String... args) throws Exception {
        String method = "run";
        if (enabled) {
            init();
            start();
            context.addListener(this);
        } else {
            logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
        }
    }

    private void init() {
        String method = "init";
        logDebug(method, "declaring topic exchange %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
        defaultExchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
        getRabbitAdmin().declareExchange(defaultExchange);

        // single message queue
        logDebug(method, "declaring queue %s", MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_QUEUE);
        Queue queue = new Queue(MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_QUEUE, true);
        getRabbitAdmin().declareQueue(queue);
        logDebug(method, "binding queue ...");
        getRabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(defaultExchange)
                .with(MqttConstants.gatewayToServerTelemetryRouting("*")));

        // batch queue
        logDebug(method, "declaring batch queue %s", MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_BATCH_QUEUE);
        Queue batchQueue = new Queue(MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_BATCH_QUEUE, true);
        getRabbitAdmin().declareQueue(batchQueue);
        logDebug(method, "binding batch queue ...");
        getRabbitAdmin().declareBinding(BindingBuilder.bind(batchQueue).to(defaultExchange)
                .with(MqttConstants.gatewayToServerTelemetryBatchRouting("*")));

        // gzip-batch queue
        logDebug(method, "declaring gzip-batch queue: %s", MqttConstants.DEFAULT_RABBITMQ_TELEMTRY_GZIP_BATCH_QUEUE);
        Queue gzipBatchQueue = new Queue(MqttConstants.DEFAULT_RABBITMQ_TELEMTRY_GZIP_BATCH_QUEUE, true);
        getRabbitAdmin().declareQueue(gzipBatchQueue);
        logDebug(method, "binding gzip-batch queue");
        getRabbitAdmin().declareBinding(BindingBuilder.bind(gzipBatchQueue).to(defaultExchange)
                .with(MqttConstants.gatewayToServerTelemetryGzipBatchRouting("*")));

        setQueues(new String[] { MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_QUEUE,
                MqttConstants.DEFAULT_RABBITMQ_TELEMETRY_BATCH_QUEUE });

        logDebug(method, "declaring direct exchange %s", MqttConstants.APPLICATION_TELEMETRY_EXCHANGE);
        appTelemetryExchange = new DirectExchange(MqttConstants.APPLICATION_TELEMETRY_EXCHANGE, true, false);
        getRabbitAdmin().declareExchange(appTelemetryExchange);
        checkCreateAppTelemetryQueues();
    }

    @Override
    public void applicationListChanged(Collection<String> applicationIds) {
        String method = "applicationListChanged";
        logInfo(method, "...");
        checkCreateAppTelemetryQueues();
    }

    @Override
    public void applicationSettingsChanged(String applicationId) {
        String method = "applicationSettingsChanged";
        logInfo(method, "...");
        checkCreateAppTelemetryQueue(
                kronosApplicationService.getKronosApplicationRepository().findByApplicationId(applicationId));
    }

    @Override
    protected int getNumWorkerThreads() {
        return numThreads;
    }

    @Override
    protected int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    @Override
    protected int getPrefetchCount() {
        return prefetchCount;
    }

    @Override
    public void receiveMessage(byte[] message, String queueName) {
        String method = "receiveMessage";
        if (MqttConstants.isGatewayToServerTelemetryRouting(queueName)) {
            blockDispatch(new Worker(message));
        } else if (MqttConstants.isGatewayToServerTelemetryBatchRouting(queueName)) {
            blockDispatch(new BatchWorker(message));
        } else {
            logError(method, "ERROR: queueName not supported: %s", queueName);
        }
    }

    private void checkCreateAppTelemetryQueues() {
        String method = "checkCreateApplicationTelemetryQueues";

        Map<String, KronosApplication> map = kronosApplicationService.getKronosApplicationRepository().findAll()
                .stream().collect(Collectors.toMap(KronosApplication::getApplicationId, Function.identity()));
        logInfo(method, "map size: %d", map.size());
        for (String appId : context.getApplicationIds().keySet()) {
            checkCreateAppTelemetryQueue(map.get(appId));
        }
    }

    private void checkCreateAppTelemetryQueue(KronosApplication kronosApp) {
        String method = "checkCreateAppTelemetryQueue";
        try {
            if (kronosApp != null && kronosApp.isLiveTelemetryStreamingEnabled()) {
                Application app = context.getCoreCacheService().findApplicationById(kronosApp.getApplicationId());
                checkEnabled(app, "application");
                String queueName = MqttConstants.applicationTelemetryRouting(app.getHid());
                logInfo(method, "creating queue: %s", queueName);
                Queue queue = new Queue(queueName, true);
                getRabbitAdmin().declareQueue(queue);
                logInfo(method, "binding queue: %s", queueName);
                getRabbitAdmin().declareBinding(
                        BindingBuilder.bind(new Queue(queueName, true)).to(appTelemetryExchange).with(queueName));
            }
        } catch (Throwable t) {
            logError(method, "unable to create queue", t);
        }
    }

    private class Worker implements Runnable {
        private final byte[] message;

        public Worker(byte[] message) {
            this.message = message;
        }

        @Override
        public void run() {
            String method = "Worker.run";
            try {
                Map<String, String> payload = JsonUtils.fromJsonBytes(message, MAP_TYPE_REF);
                logInfo(method, "payload keys: %d", payload.size());
                context.getTelemetryProcessor().process(payload);
            } catch (Throwable t) {
                logError(method, "error processing message", t);
            }
        }
    }

    private class BatchWorker implements Runnable {
        private final byte[] message;

        public BatchWorker(byte[] message) {
            this.message = message;
        }

        @Override
        public void run() {
            String method = "BatchWorker.run";
            try {
                List<Map<String, String>> payload = JsonUtils.fromJsonBytes(message, LIST_MAP_TYPE_REF);
                logInfo(method, "payload batch size: %d", payload.size());
                payload.forEach(each -> {
                    context.getTelemetryProcessor().process(each);
                });
            } catch (Throwable t) {
                logError(method, "error processing message", t);
            }
        }
    }
}
