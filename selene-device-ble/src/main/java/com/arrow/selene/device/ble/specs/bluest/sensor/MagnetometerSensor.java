package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.sensor.FloatCubeSensorData;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.device.sensor.SensorDataAbstract;
import com.arrow.selene.engine.EngineConstants;

public class MagnetometerSensor extends BleSensorAbstract<MagnetometerSensorProperties, SensorDataAbstract<?>> {
	private static final String MAGNETOMETER_X = "magnetometerX";
	private static final String MAGNETOMETER_Y = "magnetometerY";
	private static final String MAGNETOMETER_Z = "magnetometerZ";
	private static final String MAGNETOMETER_XYZ = "magnetometerXYZ";
	private static final String UUID = BlueST.getUuid(Feature.MAGNETOMETER);

	{
		payloadSize = 6; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] data, int offset) {
		List<SensorDataAbstract<?>> list = new ArrayList<>(3);
		float magX = BleUtils.shortSignedAtOffset(data, offset);
		float magY = BleUtils.shortSignedAtOffset(data, 2 + offset);
		float magZ = BleUtils.shortSignedAtOffset(data, 4 + offset);
		list.add(new FloatSensorData(MAGNETOMETER_X, magX, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(MAGNETOMETER_Y, magY, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(MAGNETOMETER_Z, magZ, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatCubeSensorData(MAGNETOMETER_XYZ, new float[] { magX, magY, magZ },
		        EngineConstants.FORMAT_DECIMAL_8));
		return Collections.unmodifiableList(list);
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] data) {
		// not implemented
		return null;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public String[] getUUIDs() {
		return new String[] { UUID };
	}

	@Override
	protected MagnetometerSensorProperties createProperties() {
		return new MagnetometerSensorProperties();
	}
}
