package com.arrow.selene.device.ble.thunderboard.react.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.thunderboard.react.GattConstants;
import com.arrow.selene.device.sensor.IntegerSensorData;

public class KeysSensor extends BleSensorAbstract<KeysSensorProperties, IntegerSensorData> {
	private static final String KEYS = "keys";
	private static final String[] UUIDs = { GattConstants.UUID_KEYS_SENSOR };

	@Override
	public void enable() {
		getBluetoothGatt().enableNotification(GattConstants.UUID_KEYS_SENSOR, GattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().disableNotification(GattConstants.UUID_KEYS_SENSOR, GattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] bytes) {
		return Collections.singletonList(new IntegerSensorData(KEYS, (int) bytes[0]));
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	protected KeysSensorProperties createProperties() {
		return new KeysSensorProperties();
	}
}
