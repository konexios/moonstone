package com.arrow.selene.device.ble.sensortag.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.sensortag.SensorTagGattConstants;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;

public class PressureSensor extends BleSensorAbstract<PressureSensorProperties, FloatSensorData> {
	private static final float SCALE = 100.0f;
	private static final String PRESSURE = "pressure";
	private static final String[] UUIDs = { SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_DATA };

	@Override
	public void enable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_CONFIG, "01");
		getBluetoothGatt().enableNotification(SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_DATA,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_CONFIG, "00");
		getBluetoothGatt().disableNotification(SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_DATA,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		getBluetoothGatt().setPeriod(SensorTagGattConstants.UUID_BAROMETER_PRESSURE_SENSOR_PERIOD,
		        String.format("%02x", period / 10));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes) {
		float pressure;
		if (bytes.length > 4) {
			pressure = BleUtils.twentyFourBitUnsignedAtOffset(bytes, 3) / SCALE;
		} else {
			Integer pre = BleUtils.shortUnsignedAtOffset(bytes, 2);
			int mantissa = pre & 0x0FFF;
			int exponent = pre >> 12 & 0xFF;
			float magnitude = (float) Math.pow(2.0, exponent);
			float output = mantissa * magnitude;
			pressure = output / 100.0f;
		}
		return Collections.singletonList(new FloatSensorData(PRESSURE, pressure, EngineConstants.FORMAT_DECIMAL_2));
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
	protected PressureSensorProperties createProperties() {
		return new PressureSensorProperties();
	}
}
