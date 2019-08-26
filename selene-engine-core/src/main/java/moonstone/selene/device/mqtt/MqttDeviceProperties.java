package moonstone.selene.device.mqtt;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;
import moonstone.selene.engine.EngineConstants;

public class MqttDeviceProperties extends DeviceProperties {
    private static final long serialVersionUID = -1287317635618202003L;

    private String mqttUrl = EngineConstants.Mqtt.DEFAULT_URL;
    private String mqttUserName = EngineConstants.Mqtt.DEFAULT_USERNAME;
    private String mqttPassword = EngineConstants.Mqtt.DEFAULT_PASSWORD;

    @Override
    public MqttDeviceProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        mqttUrl = map.getOrDefault("mqttUrl", mqttUrl);
        mqttUserName = map.getOrDefault("mqttUserName", mqttUserName);
        mqttPassword = map.getOrDefault("mqttPassword", mqttPassword);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (mqttUrl != null)
            map.put("mqttUrl", mqttUrl);
        if (mqttUserName != null)
            map.put("mqttUserName", mqttUserName);
        if (mqttPassword != null)
            map.put("mqttPassword", mqttPassword);
        return map;
    }

    public String getMqttUrl() {
        return mqttUrl;
    }

    public void setMqttUrl(String mqttUrl) {
        this.mqttUrl = mqttUrl;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }
}
