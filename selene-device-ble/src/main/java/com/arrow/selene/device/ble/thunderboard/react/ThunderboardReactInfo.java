package com.arrow.selene.device.ble.thunderboard.react;

import com.arrow.selene.device.ble.BleInfo;

public class ThunderboardReactInfo extends BleInfo {
    private static final long serialVersionUID = -7276263362815221031L;

    public static final String DEFAULT_DEVICE_TYPE = "silabs-thunderboard-react";

    public ThunderboardReactInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
