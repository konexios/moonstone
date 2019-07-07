package com.arrow.selene.device.ble.sensortag.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.sensortag.SensorTagGattConstants;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;

public class HumiditySensor extends BleSensorAbstract<HumiditySensorProperties, FloatSensorData> {
	private static final String HUMIDITY = "humidity";
	private static final float SCALE = 65536.0f / 100.0f;
	private static final String[] UUIDs = { SensorTagGattConstants.UUID_HUMIDITY_SENSOR_DATA };

	@Override
	public void enable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_HUMIDITY_SENSOR_CONFIG, "01");
		getBluetoothGatt().enableNotification(SensorTagGattConstants.UUID_HUMIDITY_SENSOR_DATA,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_HUMIDITY_SENSOR_CONFIG, "00");
		getBluetoothGatt().disableNotification(SensorTagGattConstants.UUID_HUMIDITY_SENSOR_DATA,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		getBluetoothGatt().setPeriod(SensorTagGattConstants.UUID_HUMIDITY_SENSOR_PERIOD,
		        String.format("%02x", period / 10));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
		float value = BleUtils.shortUnsignedAtOffset(data, 2) / SCALE;
		return Collections.singletonList(new FloatSensorData(HUMIDITY, value, EngineConstants.FORMAT_DECIMAL_2));
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
	protected HumiditySensorProperties createProperties() {
		return new HumiditySensorProperties();
	}
}
