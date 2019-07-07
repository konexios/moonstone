package com.arrow.selene.device.conduit;

import com.arrow.selene.engine.DeviceInfo;

public class LoraDeviceInfo extends DeviceInfo {
	private static final long serialVersionUID = -8354217654650970489L;

	public final static String DEFAULT_DEVICE_TYPE = "lora-device";

	public LoraDeviceInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
