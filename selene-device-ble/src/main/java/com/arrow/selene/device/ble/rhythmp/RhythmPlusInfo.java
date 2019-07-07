package com.arrow.selene.device.ble.rhythmp;

import com.arrow.selene.device.ble.BleInfo;

public class RhythmPlusInfo extends BleInfo {
    private static final long serialVersionUID = 5099659713706569196L;

    public static final String DEFAULT_DEVICE_TYPE = "rhythm-plus";

    public RhythmPlusInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
