package moonstone.selene.device.ble.beacon;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;
import moonstone.selene.engine.EngineConstants;

public class BeaconControllerProperties extends DeviceProperties {
    private static final long serialVersionUID = 6419713597019994874L;

    private long beaconMaxPollingIntervalMs = EngineConstants.DEFAULT_MAX_POLLING_INTERVAL_MS;
    private boolean useDbus = false;

    @Override
    public BeaconControllerProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        beaconMaxPollingIntervalMs = Long
                .parseLong(map.getOrDefault("beaconMaxPollingIntervalMs", Long.toString(beaconMaxPollingIntervalMs)));
        useDbus = Boolean.parseBoolean(map.getOrDefault("useDbus", Boolean.toString(useDbus)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("beaconMaxPollingIntervalMs", Long.toString(beaconMaxPollingIntervalMs));
        map.put("useDbus", Boolean.toString(useDbus));
        return map;
    }

    public boolean isUseDbus() {
        return useDbus;
    }

    public void setUseDbus(boolean useDbus) {
        this.useDbus = useDbus;
    }

    public long getBeaconMaxPollingIntervalMs() {
        return beaconMaxPollingIntervalMs;
    }

    public void setBeaconMaxPollingIntervalMs(long beaconMaxPollingIntervalMs) {
        this.beaconMaxPollingIntervalMs = beaconMaxPollingIntervalMs;
    }
}
