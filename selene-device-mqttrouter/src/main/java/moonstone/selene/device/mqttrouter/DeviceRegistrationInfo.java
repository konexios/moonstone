package moonstone.selene.device.mqttrouter;

import java.util.List;
import java.util.Map;

public class DeviceRegistrationInfo {

    private String deviceUid;
    private String deviceName;
    private String deviceType;
    private List<Map<String, ?>> properties;
    private Map<?, ?> metadata;

    @SuppressWarnings("unchecked")
    public DeviceRegistrationInfo populateFrom(Map<String, Object> map) {
        deviceUid = (String) map.getOrDefault("deviceUid", deviceUid);
        deviceName = (String) map.getOrDefault("deviceName", deviceName);
        deviceType = (String) map.getOrDefault("deviceType", deviceType);
        properties = (List<Map<String, ?>>) map.getOrDefault("properties", properties);
        metadata = (Map<?, ?>) map.getOrDefault("metadata", metadata);
        return this;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public List<Map<String, ?>> getProperties() {
        return properties;
    }

    public void setProperties(List<Map<String, ?>> properties) {
        this.properties = properties;
    }

    public Map<?, ?> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<?, ?> metadata) {
        this.metadata = metadata;
    }

}
