package com.arrow.selene.device.monnit;

import java.io.Serializable;
import java.util.Map;

import com.arrow.selene.engine.DeviceInfo;

public abstract class SensorInfoAbstract extends DeviceInfo implements Serializable {
    private static final long serialVersionUID = 5672302220422596249L;

    private long sensorId;
    private int profileId;
    private String profileName;
    private String version;
    private String platform;
    private double reportInterval;

    public SensorInfoAbstract() {
    }

    @Override
    public SensorInfoAbstract populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        sensorId = Long.parseLong(map.getOrDefault("sensorId", Long.toString(sensorId)));
        profileId = Integer.parseInt(map.getOrDefault("profileId", Integer.toString(profileId)));
        profileName = map.getOrDefault("profileName", profileName);
        version = map.getOrDefault("version", version);
        platform = map.getOrDefault("platform", platform);
        reportInterval = Double.parseDouble(map.getOrDefault("reportInterval", Double.toString(reportInterval)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("sensorId", Long.toString(sensorId));
        map.put("profileId", Integer.toString(profileId));
        if (profileName != null)
            map.put("profileName", profileName);
        if (version != null)
            map.put("version", version);
        if (platform != null)
            map.put("platform", platform);
        map.put("reportInterval", Double.toString(reportInterval));
        return map;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public double getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(double reportInterval) {
        this.reportInterval = reportInterval;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
