package moonstone.selene.device.ble;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;
import moonstone.selene.engine.EngineConstants;

public class BleProperties extends DeviceProperties {
    private static final long serialVersionUID = -2707645602414854356L;

    private int retryInterval;
    private boolean useDbus = false;
    private long sampleIntervalMs = EngineConstants.DEFAULT_BLE_TELEMETRY_SENDING_INTERVAL_MS;

    @Override
    public BleProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        retryInterval = Integer.parseInt(map.getOrDefault("retryInterval", Integer.toString(retryInterval)));
        useDbus = Boolean.parseBoolean(map.getOrDefault("useDbus", Boolean.toString(useDbus)));
        sampleIntervalMs = Long.parseLong(map.getOrDefault("sampleIntervalMs", Long.toBinaryString(sampleIntervalMs)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("retryInterval", Integer.toString(retryInterval));
        map.put("useDbus", Boolean.toString(useDbus));
        map.put("sampleIntervalMs", Long.toString(sampleIntervalMs));
        return map;
    }

    public boolean isUseDbus() {
        return useDbus;
    }

    public void setUseDbus(boolean useDbus) {
        this.useDbus = useDbus;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public long getSampleIntervalMs() {
        return sampleIntervalMs;
    }

    public void setSampleIntervalMs(long sampleIntervalMs) {
        this.sampleIntervalMs = sampleIntervalMs;
    }
}
