package com.arrow.selene.device.monnit;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class MonnitProperties extends DeviceProperties {
    private static final long serialVersionUID = -2612224758436102455L;

    private double sensorReportInterval = 1;
    private boolean gatewayObserveAware = false;
    private double gatewayNetworkListInterval = 1;
    private double gatewayPollInterval = 1;
    private double gatewayReportInterval = 1;

    @Override
    public MonnitProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        sensorReportInterval = Double
                .parseDouble(map.getOrDefault("sensorReportInterval", Double.toString(sensorReportInterval)));
        gatewayObserveAware = Boolean
                .parseBoolean(map.getOrDefault("gatewayObserveAware", Boolean.toString(gatewayObserveAware)));
        gatewayNetworkListInterval = Double.parseDouble(
                map.getOrDefault("gatewayNetworkListInterval", Double.toString(gatewayNetworkListInterval)));
        gatewayPollInterval = Double
                .parseDouble(map.getOrDefault("gatewayPollInterval", Double.toString(gatewayPollInterval)));
        gatewayReportInterval = Double
                .parseDouble(map.getOrDefault("gatewayReportInterval", Double.toString(gatewayReportInterval)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        map.put("sensorReportInterval", Double.toString(sensorReportInterval));
        map.put("gatewayObserveAware", Boolean.toString(gatewayObserveAware));
        map.put("gatewayNetworkListInterval", Double.toString(gatewayNetworkListInterval));
        map.put("gatewayPollInterval", Double.toString(gatewayPollInterval));
        map.put("gatewayReportInterval", Double.toString(gatewayReportInterval));
        return map;
    }

    public double getSensorReportInterval() {
        return sensorReportInterval;
    }

    public void setSensorReportInterval(double sensorReportInterval) {
        this.sensorReportInterval = sensorReportInterval;
    }

    public boolean isGatewayObserveAware() {
        return gatewayObserveAware;
    }

    public void setGatewayObserveAware(boolean gatewayObserveAware) {
        this.gatewayObserveAware = gatewayObserveAware;
    }

    public double getGatewayNetworkListInterval() {
        return gatewayNetworkListInterval;
    }

    public void setGatewayNetworkListInterval(double gatewayNetworkListInterval) {
        this.gatewayNetworkListInterval = gatewayNetworkListInterval;
    }

    public double getGatewayPollInterval() {
        return gatewayPollInterval;
    }

    public void setGatewayPollInterval(double gatewayPollInterval) {
        this.gatewayPollInterval = gatewayPollInterval;
    }

    public double getGatewayReportInterval() {
        return gatewayReportInterval;
    }

    public void setGatewayReportInterval(double gatewayReportInterval) {
        this.gatewayReportInterval = gatewayReportInterval;
    }
}
