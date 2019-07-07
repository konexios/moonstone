package com.arrow.selene.device.ble;

import java.util.Map;

import com.arrow.selene.device.ble.sensor.BleSensor;
import com.arrow.selene.engine.DeviceModule;

public interface BleModule<Info extends BleInfo, Prop extends BleProperties, State extends BleStates, Data extends
		BleData> extends DeviceModule<Info, Prop, State, Data> {

	Map<String, BleSensor<?, ?>> getSensors();

	boolean isRandomAddress();

	void addHandleToChar(String handle, String characteristic);

	String getCharacteristic(String handle);
}
