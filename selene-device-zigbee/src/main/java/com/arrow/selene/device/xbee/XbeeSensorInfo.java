package com.arrow.selene.device.xbee;

import java.util.Map;

import com.arrow.selene.engine.DeviceInfo;

public class XbeeSensorInfo extends DeviceInfo {
    private static final long serialVersionUID = -5320262258290852666L;

    public static final String DEFAULT_DEVICE_TYPE = "xbee-sensor";

    private String port;
    private int baudRate;
    private String nodeId;

    public XbeeSensorInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public XbeeSensorInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        port = map.getOrDefault("port", port);
        baudRate = Integer.parseInt(map.getOrDefault("baudRate", Integer.toString(baudRate)));
        nodeId = map.getOrDefault("nodeId", nodeId);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (port != null)
            map.put("port", port);
        map.put("baudRate", Integer.toString(baudRate));
        if (nodeId != null)
            map.put("nodeId", nodeId);
        return map;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
