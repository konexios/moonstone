package com.arrow.selene.device.ble.beacon;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.engine.DeviceInfo;
import com.arrow.selene.engine.Utils;

public class BeaconControllerInfo extends DeviceInfo {
    private static final long serialVersionUID = -3972672290102699103L;

    public final static String DEFAULT_DEVICE_TYPE = "ble-beacon-controller";

    private String bleInterface;
    private String deviceMapping;

    private Map<String, String> mapping = new HashMap<>();

    public BeaconControllerInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public BeaconControllerInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        bleInterface = map.getOrDefault("bleInterface", bleInterface);
        deviceMapping = map.getOrDefault("deviceMapping", deviceMapping);
        String value = map.get("mapping");
        if (StringUtils.isNotEmpty(value)) {
            try {
                mapping = JsonUtils.fromJson(value, Utils.GENERIC_MAP_TYPE_REF);
            } catch (Exception e) {
            }
        }
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (bleInterface != null)
            map.put("bleInterface", bleInterface);
        if (deviceMapping != null)
            map.put("deviceMapping", deviceMapping);
        if (mapping != null && mapping.size() > 0)
            map.put("mapping", JsonUtils.toJson(mapping));
        return map;
    }

    public String getBleInterface() {
        return bleInterface;
    }

    public void setBleInterface(String bleInterface) {
        this.bleInterface = bleInterface;
    }

    public String getDeviceMapping() {
        return deviceMapping;
    }

    public void setDeviceMapping(String deviceMapping) {
        this.deviceMapping = deviceMapping;

        String[] tokens = deviceMapping.split("\\|", -1);
        for (String token : tokens) {
            String[] pair = token.split(":", -1);
            if (pair.length == 2) {
                mapping.put(pair[0].trim(), pair[1].trim());
            }
        }
    }

    public String findMapping(String key) {
        return mapping.get(key);
    }
}
