package com.arrow.selene.device.ble;

import java.util.Map;

import com.arrow.selene.engine.DeviceInfo;

public class BleInfo extends DeviceInfo {
    private static final long serialVersionUID = 3789890719720285556L;

    private String bleInterface;
    private String bleAddress;

    private String deviceName;
    private String firmwareRevision;
    private String manufacturerName;
    private String modelNumber;
    private String serialNumber;
    private String softwareRevision;

    @Override
    public BleInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        bleInterface = map.getOrDefault("bleInterface", bleInterface);
        bleAddress = map.getOrDefault("bleAddress", bleAddress);
        deviceName = map.getOrDefault("deviceName", deviceName);
        firmwareRevision = map.getOrDefault("firmwareRevision", firmwareRevision);
        manufacturerName = map.getOrDefault("manufacturerName", manufacturerName);
        modelNumber = map.getOrDefault("modelNumber", modelNumber);
        serialNumber = map.getOrDefault("serialNumber", serialNumber);
        softwareRevision = map.getOrDefault("softwareRevision", softwareRevision);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (bleInterface != null)
            map.put("bleInterface", bleInterface);
        if (bleAddress != null)
            map.put("bleAddress", bleAddress);
        if (deviceName != null)
            map.put("deviceName", deviceName);
        if (firmwareRevision != null)
            map.put("firmwareRevision", firmwareRevision);
        if (manufacturerName != null)
            map.put("manufacturerName", manufacturerName);
        if (modelNumber != null)
            map.put("modelNumber", modelNumber);
        if (serialNumber != null)
            map.put("serialNumber", serialNumber);
        if (softwareRevision != null)
            map.put("softwareRevision", softwareRevision);
        return map;
    }

    public String getBleAddress() {
        return bleAddress;
    }

    public void setBleAddress(String bleAddress) {
        this.bleAddress = bleAddress;
    }

    public String getBleInterface() {
        return bleInterface;
    }

    public void setBleInterface(String bleInterface) {
        this.bleInterface = bleInterface;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftwareRevision() {
        return softwareRevision;
    }

    public void setSoftwareRevision(String softwareRevision) {
        this.softwareRevision = softwareRevision;
    }
}
