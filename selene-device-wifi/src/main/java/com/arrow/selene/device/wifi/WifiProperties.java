package com.arrow.selene.device.wifi;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class WifiProperties extends DeviceProperties {
    private static final long serialVersionUID = 72123226615101867L;

    private static final long DEFAULT_ACCESS_POINTS_POLLING_MS = 60000L;

    private String deviceName;
    private String ssid;
    private String authAlg;
    private String keyMgmt;
    private String password;
    private String mode = WifiMode.MONITOR.name();
    private long accessPointsPollingMs = DEFAULT_ACCESS_POINTS_POLLING_MS;

    @Override
    public WifiProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        deviceName = map.getOrDefault("deviceName", deviceName);
        ssid = map.getOrDefault("ssid", ssid);
        authAlg = map.getOrDefault("authAlg", authAlg);
        keyMgmt = map.getOrDefault("keyMgmt", keyMgmt);
        password = map.getOrDefault("password", password);
        mode = map.getOrDefault("mode", mode);
        accessPointsPollingMs = Long
                .parseLong(map.getOrDefault("accessPointsPollingMs", Long.toString(accessPointsPollingMs)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (deviceName != null)
            map.put("deviceName", deviceName);
        if (ssid != null)
            map.put("ssid", ssid);
        if (authAlg != null)
            map.put("authAlg", authAlg);
        if (keyMgmt != null)
            map.put("keyMgmt", keyMgmt);
        if (password != null)
            map.put("password", password);
        if (mode != null)
            map.put("mode", mode);
        map.put("accessPointsPollingMs", Long.toString(accessPointsPollingMs));
        return map;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getAuthAlg() {
        return authAlg;
    }

    public void setAuthAlg(String authAlg) {
        this.authAlg = authAlg;
    }

    public String getKeyMgmt() {
        return keyMgmt;
    }

    public void setKeyMgmt(String keyMgmt) {
        this.keyMgmt = keyMgmt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getAccessPointsPollingMs() {
        return accessPointsPollingMs;
    }

    public void setAccessPointsPollingMs(long accessPointsPollingMs) {
        this.accessPointsPollingMs = accessPointsPollingMs;
    }
}
