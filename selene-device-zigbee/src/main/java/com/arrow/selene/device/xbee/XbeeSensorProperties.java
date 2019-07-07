package com.arrow.selene.device.xbee;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class XbeeSensorProperties extends DeviceProperties {
    private static final long serialVersionUID = -7558655554471670768L;

    private int pollingInterval;
    private int retryInterval;

    @Override
    public XbeeSensorProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        pollingInterval = Integer.parseInt(map.getOrDefault("pollingInterval", Integer.toString(pollingInterval)));
        retryInterval = Integer.parseInt(map.getOrDefault("retryInterval", Integer.toString(retryInterval)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("pollingInterval", Integer.toString(pollingInterval));
        map.put("retryInterval", Integer.toString(retryInterval));
        return map;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }
}
