package com.arrow.selene.device.ble.sensortag.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.sensortag.SensorTagGattConstants;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;

public class TemperatureSensor extends BleSensorAbstract<TemperatureSensorProperties, FloatSensorData> {
	private static final float SCALE = 128.0f;
	private static final String SURFACE_TEMPERATURE = "surfaceTemperature";
	private static final String AMBIENT_TEMPERATURE = "ambientTemperature";
	private static final String[] UUIDs = { SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_DATA };

	@Override
	public void enable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_CONFIG, "01");
		getBluetoothGatt().enableNotification(SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_DATA,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_CONFIG, "00");
		getBluetoothGatt().disableNotification(SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_DATA,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		getBluetoothGatt().setPeriod(SensorTagGattConstants.UUID_TEMPERATURE_SENSOR_PERIOD,
		        String.format("%02x", period / 10));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes) {
		List<FloatSensorData> result = new ArrayList<>(2);
		result.add(new FloatSensorData(SURFACE_TEMPERATURE, BleUtils.shortUnsignedAtOffset(bytes, 0) / SCALE,
		        EngineConstants.FORMAT_DECIMAL_2));
		result.add(new FloatSensorData(AMBIENT_TEMPERATURE, BleUtils.shortUnsignedAtOffset(bytes, 2) / SCALE,
		        EngineConstants.FORMAT_DECIMAL_2));
		return Collections.unmodifiableList(result);
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
	protected TemperatureSensorProperties createProperties() {
		return new TemperatureSensorProperties();
	}
}
