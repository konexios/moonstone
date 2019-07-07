package com.arrow.selene.device.ble.sensortag;

import com.arrow.selene.device.ble.BleInfo;

public class SensorTagInfo extends BleInfo {
    private static final long serialVersionUID = -8300883915523509163L;

    public static final String DEFAULT_DEVICE_TYPE = "ti-sensortag";

    public SensorTagInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
