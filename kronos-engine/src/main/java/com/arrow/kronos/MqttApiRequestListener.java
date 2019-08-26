package com.arrow.kronos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import com.arrow.pegasus.service.RabbitListenerAbstract;

import moonstone.acn.MqttConstants;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.CloudRequestModel;

public class MqttApiRequestListener extends RabbitListenerAbstract implements CommandLineRunner {

    private TopicExchange defaultExchange;

    @Value("${MqttApiRequestListener.enabled:true}")
    private boolean enabled;
    @Value("${MqttApiRequestListener.numThreads:10}")
    private int numThreads;
    @Value("${MqttApiRequestListener.concurrentConsumers:5}")
    private int concurrentConsumers;
    @Value("${MqttApiRequestListener.prefetchCount:10}")
    private int prefetchCount;
    @Value("${MqttApiRequestListener.kronos.api.baseUrl:}")
    private String kronosApiBaseUrl;

    @Autowired
    private ApiRequestProcessor processor;

    @Override
    public void run(String... args) throws Exception {
        String method = "run";
        if (enabled) {
            if (StringUtils.isNoneBlank(kronosApiBaseUrl)) {
                init();
                start();
            } else {
                logWarn(method, "BaseURL is not defined!");
            }
        } else {
            logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
        }
    }

    private void init() {
        String method = "init";

        logDebug(method, "declaring topic exchange %s", MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
        defaultExchange = new TopicExchange(MqttConstants.DEFAULT_RABBITMQ_EXCHANGE);
        getRabbitAdmin().declareExchange(defaultExchange);

        logDebug(method, "declaring queue %s", MqttConstants.DEFAULT_RABBITMQ_MQTT_API_QUEUE);
        Queue queue = new Queue(MqttConstants.DEFAULT_RABBITMQ_MQTT_API_QUEUE, true);
        getRabbitAdmin().declareQueue(queue);

        String routingKey = MqttConstants.gatewayToServerMqttApiRouting("*");
        logDebug(method, "binding queue with routingKey %s", routingKey);
        getRabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(defaultExchange).with(routingKey));

        setQueues(new String[] { MqttConstants.DEFAULT_RABBITMQ_MQTT_API_QUEUE });
    }

    @Override
    public void receiveMessage(byte[] message, String queueName) {
        String method = "receiveMessage";
        logDebug(method, "%s %s", new String(message), queueName);
        blockDispatch(() -> {
            try {
                String gatewayHid = queueName.substring(queueName.lastIndexOf('.') + 1);
                logDebug(method, "gatewayHid=%s", gatewayHid);
                CloudRequestModel request = JsonUtils.fromJsonBytes(message, CloudRequestModel.class);
                processor.process(gatewayHid, kronosApiBaseUrl, request);
            } catch (Exception e) {
                logError(method, "Error processing message", e);
            }
        });
    }

    @Override
    protected int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    @Override
    protected int getNumWorkerThreads() {
        return numThreads;
    }

    @Override
    protected int getPrefetchCount() {
        return prefetchCount;
    }
}
