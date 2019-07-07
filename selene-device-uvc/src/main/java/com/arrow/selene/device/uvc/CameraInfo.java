package com.arrow.selene.device.uvc;

import com.arrow.selene.engine.DeviceInfo;

public class CameraInfo extends DeviceInfo {
    private static final long serialVersionUID = -8780123863961891538L;

    public static final String DEFAULT_DEVICE_TYPE = "camera";

    public CameraInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
