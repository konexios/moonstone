package com.arrow.selene.device.cellular;

import java.util.Map;

import com.arrow.selene.engine.DeviceInfo;

public class CellularModemInfo extends DeviceInfo {
    private static final long serialVersionUID = 3872764497435363222L;

    public static final String DEFAULT_DEVICE_TYPE = "cellular-modem";

    private String manufacturer;
    private String model;
    private String revision;
    private String phone;
    private String imei;
    private String imsi;
    private String iccid;

    public CellularModemInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public CellularModemInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        manufacturer = map.getOrDefault("manufacturer", manufacturer);
        model = map.getOrDefault("model", model);
        revision = map.getOrDefault("revision", revision);
        phone = map.getOrDefault("phone", phone);
        imei = map.getOrDefault("imei", imei);
        imsi = map.getOrDefault("imsi", imsi);
        iccid = map.getOrDefault("iccid", iccid);
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (manufacturer != null)
            map.put("manufacturer", manufacturer);
        if (model != null)
            map.put("model", model);
        if (revision != null)
            map.put("revision", revision);
        if (phone != null)
            map.put("phone", phone);
        if (imei != null)
            map.put("imei", imei);
        if (imsi != null)
            map.put("imsi", imsi);
        if (iccid != null)
            map.put("iccid", iccid);
        return map;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}
