package com.arrow.selene.device.libelium;

import com.arrow.selene.device.libelium.data.SensorParser;
import com.arrow.selene.engine.DeviceModule;

public interface WaspmoteModule extends DeviceModule<WaspmoteInfo, WaspmoteProperties, WaspmoteStates,
		WaspmoteData> {
	public void processSensorData(SensorParser data);
}
