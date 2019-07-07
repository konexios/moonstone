package com.arrow.selene.device.ble.thunderboard.sense.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.thunderboard.sense.GattConstants;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;

public class LightSensor extends BleSensorAbstract<LightSensorProperties, FloatSensorData> {
	private static final String LIGHT = "light";
	private static final float SCALE = 100.0f;
	private static final String[] UUIDs = { GattConstants.UUID_AMBIENT_LIGHT_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
		float value = BleUtils.thirtyTwoBitUnsignedAtOffset(data, 0) / SCALE;
		return Collections.singletonList(new FloatSensorData(LIGHT, value, EngineConstants.FORMAT_DECIMAL_2));
	}

	@Override
	protected LightSensorProperties createProperties() {
		return new LightSensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_AMBIENT_LIGHT_SENSOR);
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	public boolean isPassive() {
		return true;
	}
}
