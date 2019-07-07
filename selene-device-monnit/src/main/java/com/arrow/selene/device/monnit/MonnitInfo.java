package com.arrow.selene.device.monnit;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.engine.DeviceInfo;
import com.monnit.mine.MonnitMineAPI.enums.eMineListenerProtocol;

public class MonnitInfo extends DeviceInfo {
    private static final long serialVersionUID = 1582789024846681775L;

    public static final String DEFAULT_DEVICE_TYPE = "monnit-gateway";

    private String protocol = eMineListenerProtocol.TCPAndUDP.name();
    private String listeningAddress = "0.0.0.0";
    private int listeningPort = 3000;
    private long gatewayId;
    private String gatewayType;
    private String gatewayFirmwareVersion = "3.4.1.3";
    private String radioFirmwareVersion = "1.0.1.0";
    private GenericSensorsHolder sensors = new GenericSensorsHolder();

    public MonnitInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public MonnitInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        protocol = map.getOrDefault("protocol", protocol);
        listeningAddress = map.getOrDefault("listeningAddress", listeningAddress);
        listeningPort = Integer.parseInt(map.getOrDefault("listeningPort", Integer.toString(listeningPort)));
        gatewayId = Integer.parseInt(map.getOrDefault("gatewayId", Long.toString(gatewayId)));
        gatewayType = map.getOrDefault("gatewayType", gatewayType);
        gatewayFirmwareVersion = map.getOrDefault("gatewayFirmwareVersion", gatewayFirmwareVersion);
        radioFirmwareVersion = map.getOrDefault("radioFirmwareVersion", radioFirmwareVersion);
        String value = map.get("sensors");
        if (StringUtils.isNotEmpty(value)) {
            sensors = JsonUtils.fromJson(value, GenericSensorsHolder.class);
        }
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (protocol != null)
            map.put("protocol", protocol);
        if (listeningAddress != null)
            map.put("listeningAddress", listeningAddress);
        map.put("listeningPort", Integer.toString(listeningPort));
        map.put("gatewayId", Long.toString(gatewayId));
        if (gatewayType != null)
            map.put("gatewayType", gatewayType);
        if (gatewayFirmwareVersion != null)
            map.put("gatewayFirmwareVersion", gatewayFirmwareVersion);
        if (radioFirmwareVersion != null)
            map.put("radioFirmwareVersion", radioFirmwareVersion);
        if (sensors != null) {
            map.put("sensors", JsonUtils.toJson(sensors));
        }
        return map;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getListeningAddress() {
        return listeningAddress;
    }

    public void setListeningAddress(String listeningAddress) {
        this.listeningAddress = listeningAddress;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public void setListeningPort(int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(long gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getGatewayFirmwareVersion() {
        return gatewayFirmwareVersion;
    }

    public void setGatewayFirmwareVersion(String gatewayFirmwareVersion) {
        this.gatewayFirmwareVersion = gatewayFirmwareVersion;
    }

    public String getRadioFirmwareVersion() {
        return radioFirmwareVersion;
    }

    public void setRadioFirmwareVersion(String radioFirmwareVersion) {
        this.radioFirmwareVersion = radioFirmwareVersion;
    }

    public GenericSensorsHolder getSensors() {
        return sensors;
    }

    public void setSensors(GenericSensorsHolder sensors) {
        this.sensors = sensors;
    }
}
