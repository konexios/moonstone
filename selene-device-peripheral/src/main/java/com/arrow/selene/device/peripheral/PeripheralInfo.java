package com.arrow.selene.device.peripheral;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.engine.DeviceInfo;

public class PeripheralInfo extends DeviceInfo {
    private static final long serialVersionUID = 8253565241640386000L;

    public static final String DEFAULT_DEVICE_TYPE = "peripheral";

    private PeripheralInfoHolder periphery = new PeripheralInfoHolder();

    public PeripheralInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public PeripheralInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        String value = map.get("periphery");
        if (StringUtils.isNotEmpty(value)) {
            periphery = JsonUtils.fromJson(value, PeripheralInfoHolder.class);
        }
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (periphery != null)
            map.put("periphery", JsonUtils.toJson(periphery));
        return map;
    }

    public PeripheralInfoHolder getPeriphery() {
        return periphery;
    }

    public void setPeriphery(PeripheralInfoHolder periphery) {
        this.periphery = periphery;
    }
}
