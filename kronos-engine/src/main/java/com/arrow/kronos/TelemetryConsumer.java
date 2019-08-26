package com.arrow.kronos;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.Telemetry;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.util.ActionHelper;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.kafka.KafkaConsumerAbstract;
import com.arrow.pegasus.kafka.KafkaSender;
import com.arrow.pegasus.security.Crypto;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.YesNoInherit;

public class TelemetryConsumer extends KafkaConsumerAbstract implements CommandLineRunner {
    private static final TypeReference<List<TelemetryItem>> LIST_ITEM_TYPE_REF = new TypeReference<List<TelemetryItem>>() {
    };

    @Autowired
    private KronosEngineContext context;

    @Value("${TelemetryConsumer.enabled:true}")
    private boolean enabled;
    @Value("${TelemetryConsumer.numThreads:200}")
    private int numThreads;

    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private Crypto crypto;

    @Override
    protected void postConstruct() {
        super.postConstruct();
    }

    @Override
    public void run(String... args) throws Exception {
        String method = "run";
        if (enabled) {
            try {
                start();
            } catch (Exception e) {
                logError(method, e);
            }
        } else {
            logWarn(method, "%s is DISABLED!", getClass().getSimpleName());
        }
    }

    @Override
    protected Pattern getTopicPattern() {
        String method = "getTopicPattern";
        String pattern = KronosConstants.Telemetry.kafkaTopic(context.getApplicationEngine().getId());
        logInfo(method, "pattern: %s", pattern);
        return Pattern.compile(pattern);
    }

    @Override
    protected void process(final String topic, final String key, final String message) {
        String method = "TelemetryConsumer.process";
        try {
            List<TelemetryItem> data = JsonUtils.fromJson(message, LIST_ITEM_TYPE_REF);
            if (data.size() > 0) {
                TelemetryWrapper wrapper = new TelemetryWrapper();
                wrapper.setItems(data);
                TelemetryItem first = data.get(0);
                wrapper.setDeviceId(first.getDeviceId());
                wrapper.setTimestamp(first.getTimestamp());
                wrapper.setPayloadSize(message.length());

                // validate device
                Device device = context.getKronosCache().findDeviceById(wrapper.getDeviceId());
                checkEnabled(device, "device not found or not enabled: " + wrapper.getDeviceId());
                wrapper.setApplicationId(device.getApplicationId());

                // validate gateway
                Gateway gateway = context.getKronosCache().findGatewayById(device.getGatewayId());
                checkEnabled(gateway, "gateway not found or not enabled: " + device.getGatewayId());

                // validate kronosApplication
                KronosApplication kronosApp = context.getKronosCache()
                        .findKronosApplicationByApplicationId(gateway.getApplicationId());
                Assert.notNull(kronosApp, "kronosApplication not found: " + gateway.getApplicationId());

                // configuration/settings
                YesNoInherit persist = device.getPersistTelemetry();
                if (persist == null || persist == YesNoInherit.INHERIT) {
                    persist = gateway.getPersistTelemetry();
                    if (persist == null || persist == YesNoInherit.INHERIT) {
                        persist = kronosApp.getPersistTelemetry();
                    }
                }

                YesNoInherit index = device.getIndexTelemetry();
                if (index == null || index == YesNoInherit.INHERIT) {
                    index = gateway.getIndexTelemetry();
                    if (index == null || index == YesNoInherit.INHERIT) {
                        index = kronosApp.getIndexTelemetry();
                    }
                }

                // boolean hasIoTCentralWebHook = false;
                // for (Integration integration : kronosApp.getIntegrations()) {
                // if (integration.getType() ==
                // IntegrationType.IoTCentralWebHook) {
                // hasIoTCentralWebHook = integration.isEnabled();
                // break;
                // }
                // }

                boolean hasAction = false;
                for (DeviceAction deviceAction : ActionHelper.getDeviceActions(context, wrapper, device)) {
                    if (deviceAction.isEnabled()) {
                        hasAction = true;
                        break;
                    }
                }

                // persist telemetry
                boolean hasLon = false;
                boolean hasLat = false;
                Telemetry telemetry = toTelemetry(wrapper);

                if (persist == YesNoInherit.YES) {
                    context.getTelemetryService().getTelemetryRepository().doInsert(telemetry, CoreConstant.ADMIN_USER);
                    wrapper.setId(telemetry.getId());
                    logDebug(method, "persisted new telemetry: %s", wrapper.getId());
                } else {
                    logInfo(method, "skipped persistence of telemetry for device: %s / %s", device.getUid(),
                            device.getName());
                }

                for (TelemetryItem item : data) {
                    item.setApplicationId(wrapper.getApplicationId());
                    item.setTelemetryId(wrapper.getId());
                    hasLon |= item.getName().equals(KronosConstants.Telemetry.LONGITUDE);
                    hasLat |= item.getName().equals(KronosConstants.Telemetry.LATITUDE);
                }

                if (persist == YesNoInherit.YES) {
                    logInfo(method, "sending to persistence queue for device: %s / %s", device.getUid(),
                            device.getName());
                    // sendTelemetryToRabbitMQ(wrapper, ProcessorQueue.PERSISTENCE);
                    sendTelemetryToKafka(wrapper, KronosEngineConstants.KafkaTelemetryProcessor.PERSISTENCE);
                } else {
                    logInfo(method, "skipped persistence for device: %s / %s", device.getUid(), device.getName());
                }

                if (index == YesNoInherit.YES) {
                    logInfo(method, "sending to index queue for device: %s / %s", device.getUid(), device.getName());
                    // sendTelemetryToRabbitMQ(wrapper, ProcessorQueue.ES_TELEMETRY);
                    sendTelemetryToKafka(wrapper, KronosEngineConstants.KafkaTelemetryProcessor.ES_TELEMETRY);
                } else {
                    logInfo(method, "skipped indexing for device: %s / %s", device.getUid(), device.getName());
                }

                // if (hasIoTCentralWebHook) {
                // logInfo(method, "sending to IoTCentralWebHook queue for
                // device: %s / %s", device.getUid(),
                // device.getName());
                // sendTelemetryToRabbitMQ(wrapper,
                // ProcessorQueue.IOT_CENTRAL_WEBHOOK_TELEMETRY);
                // } else {
                // logInfo(method, "skipped IoTCentralWebHook for device: %s /
                // %s", device.getUid(), device.getName());
                // }

                if (hasAction) {
                    logInfo(method, "sending to action queue for device: %s / %s", device.getUid(), device.getName());
                    // sendTelemetryToRabbitMQ(wrapper, ProcessorQueue.ACTION);
                    sendTelemetryToKafka(wrapper, KronosEngineConstants.KafkaTelemetryProcessor.ACTION);
                } else {
                    logInfo(method, "skipped action for device: %s / %s", device.getUid(), device.getName());
                }

                if (hasLon && hasLat) {
                    logInfo(method, "sending to location queue for device: %s / %s", device.getUid(), device.getName());
                    // sendTelemetryToRabbitMQ(wrapper, ProcessorQueue.LOCATION);
                    sendTelemetryToKafka(wrapper, KronosEngineConstants.KafkaTelemetryProcessor.LOCATION);
                } else {
                    logInfo(method, "skipped location for device: %s / %s", device.getUid(), device.getName());
                }

                // sendTelemetryToRabbitMQ(wrapper, ProcessorQueue.LAST_TELEMETRY_ITEM);
                sendTelemetryToKafka(wrapper, KronosEngineConstants.KafkaTelemetryProcessor.LAST_TELEMETRY_ITEM);
            } else {
                logInfo(method, "data is empty");
            }
        } catch (Throwable t) {
            logError(method, t);
        }
    }

    @Override
    protected int getNumConsumers() {
        return numThreads;
    }

    private Telemetry toTelemetry(TelemetryWrapper wrapper) {
        Telemetry result = new Telemetry();
        result.setApplicationId(wrapper.getApplicationId());
        result.setDeviceId(wrapper.getDeviceId());
        result.setPayloadSize(wrapper.getPayloadSize());
        result.setTimestamp(wrapper.getTimestamp());
        return result;
    }

    //    private void sendTelemetryToRabbitMQ(TelemetryWrapper wrapper, String prefix) {
    //        context.getRabbit().convertAndSend(ProcessorQueue.EXCHANGE,
    //                ProcessorQueue.queueName(prefix, context.getApplicationEngine().getId()),
    //                JsonUtils.toJsonBytes(wrapper));
    //    }

    private void sendTelemetryToKafka(TelemetryWrapper wrapper, String suffix) {
        String method = "sendTelemetryToKafka";
        String topic = KronosEngineConstants.KafkaTelemetryProcessor.topic(context.getApplicationEngine().getId(),
                suffix);
        String key = crypto.randomToken();
        String payload = JsonUtils.toJson(wrapper);
        logInfo(method, "topic: %s, payload size: %d", topic, payload.length());
        kafkaSender.send(topic, key, payload);
    }
}
