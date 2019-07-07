package com.arrow.selene.device.ble.rhythmp;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.sensortag.SensorTagGattConstants;
import com.arrow.selene.device.sensor.IntegerSensorData;

public class HeartRateSensor extends BleSensorAbstract<HeartRateSensorProperties, IntegerSensorData> {
	private static final String HEART_RATE = "heartRate";

	// TODO Incorrect UUID. @MERA team kindly update the UUID
	private static final String[] UUIDs = { GattConstants.HANDLE_HR_SENSOR_VALUE };

	@Override
	public void enable() {
		getBluetoothGatt().enableNotification(GattConstants.HANDLE_HR_SENSOR_NOTIFICATION,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().disableNotification(GattConstants.HANDLE_HR_SENSOR_NOTIFICATION,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] bytes) {
		String method = "parseData";
		int heartRate = BleUtils.byteUnsignedAtOffset(bytes, 1);
		logInfo(method, "heartRate: %d", heartRate);
		return Collections.singletonList(new IntegerSensorData(HEART_RATE, heartRate));
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
	protected HeartRateSensorProperties createProperties() {
		return new HeartRateSensorProperties();
	}
}
