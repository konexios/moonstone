package moonstone.selene.engine.cloud;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.api.AcnClient;
import moonstone.acn.client.cloud.MqttConnectorAbstract;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsUtils;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.device.self.SelfProperties;
import moonstone.selene.engine.DeviceModule;
import moonstone.selene.engine.service.ModuleService;

public class EdgeMqttConnector extends MqttConnectorAbstract {

    public final static String DEVICE_HID = "{deviceHid}";
    public final static String DEVICE_UID = "{deviceUid}";
    public final static String DEVICE_TYPE = "{deviceType}";
    public final static String GATEWAY_HID = "{gatewayHid}";
    public final static String GATEWAY_UID = "{gatewayUid}";

    private String publisherTopic, subscriberTopic;
    private Map<String, String> deviceLookup = new ConcurrentHashMap<>();

    protected EdgeMqttConnector(String url, String gatewayHid, AcnClient acnClient) {
        super(url, gatewayHid, acnClient);
        String method = "EdgeMqttConnector";
        SelfProperties props = SelfModule.getInstance().getProperties();
        AcsUtils.notEmpty(props.getEdgeMqttPublisherTopic(),
                "required configuration: edgeMqttPublisherTopic not found");
        publisherTopic = props.getEdgeMqttPublisherTopic();
        logInfo(method, "publisherTopic: %s", publisherTopic);
        AcsUtils.notEmpty(props.getEdgeMqttSubscriberTopic(),
                "required configuration: edgeMqttSubscriberTopic not found");
        subscriberTopic = props.getEdgeMqttSubscriberTopic();
        logInfo(method, "subscriberTopic: %s", subscriberTopic);
    }

    @Override
    protected MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = super.mqttConnectOptions();
        SelfProperties props = SelfModule.getInstance().getProperties();
        if (StringUtils.isNotEmpty(props.getEdgeMqttUsername())) {
            options.setUserName(props.getEdgeMqttUsername());
        }
        if (StringUtils.isNotEmpty(props.getEdgeMqttPassword())) {
            options.setPassword(props.getEdgeMqttPassword().toCharArray());
        }
        return options;
    }

    @Override
    protected String publisherBatchTopic(List<IotParameters> batch) {
        throw new AcsLogicalException(getClass().getName() + " does not support batch mode");
    }

    @Override
    protected String publisherGzipBatchTopic(List<IotParameters> arg0) {
        throw new AcsLogicalException(getClass().getName() + " does not support batch mode");
    }

    @Override
    protected String publisherTopic(IotParameters data) {
        String deviceUid = deviceLookup.get(data.getDeviceHid());
        if (StringUtils.isEmpty(deviceUid)) {
            DeviceModule<?, ?, ?, ?> module = ModuleService.getInstance().findDevice(data.getDeviceHid());
            AcsUtils.notNull(module, "Module not found for hid: %s", data.getDeviceHid());
            deviceUid = module.getInfo().getUid();
            deviceLookup.put(data.getDeviceHid(), deviceUid);
        }
        return StringUtils.replaceEach(publisherTopic,
                new String[] { GATEWAY_HID, GATEWAY_UID, DEVICE_TYPE, DEVICE_HID, DEVICE_UID },
                new String[] { getGatewayHid(), SelfModule.getInstance().getInfo().getUid(), data.getDeviceType(),
                        data.getDeviceHid(), deviceUid });
    }

    @Override
    protected String subscriberTopic() {
        return StringUtils.replaceEach(subscriberTopic, new String[] { GATEWAY_HID, GATEWAY_UID },
                new String[] { getGatewayHid(), SelfModule.getInstance().getInfo().getUid() });

    }
}
