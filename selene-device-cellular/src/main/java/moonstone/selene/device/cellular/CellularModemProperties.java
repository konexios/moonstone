package moonstone.selene.device.cellular;

import java.util.Map;

import moonstone.selene.engine.DeviceProperties;

public class CellularModemProperties extends DeviceProperties {
    private static final long serialVersionUID = 9105453869922281171L;
    private static final long DEFAULT_HEALTH_CHECH_MS = 60000;
    private static final long DEFAULT_GPS_POLLING_MS = 60000;

    private long healthCheckMs = DEFAULT_HEALTH_CHECH_MS;
    private long gpsPollingMs = DEFAULT_GPS_POLLING_MS;

    @Override
    public CellularModemProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        healthCheckMs = Long.parseLong(map.getOrDefault("healthCheckMs", Long.toString(healthCheckMs)));
        gpsPollingMs = Long.parseLong(map.getOrDefault("gpsPollingMs", Long.toString(gpsPollingMs)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("healthCheckMs", Long.toString(healthCheckMs));
        map.put("gpsPollingMs", Long.toString(gpsPollingMs));
        return map;
    }

    public long getHealthCheckMs() {
        return healthCheckMs;
    }

    public void setHealthCheckMs(long healthCheckMs) {
        this.healthCheckMs = healthCheckMs;
    }

    public long getGpsPollingMs() {
        return gpsPollingMs;
    }

    public void setGpsPollingMs(long gpsPollingMs) {
        this.gpsPollingMs = gpsPollingMs;
    }
}
