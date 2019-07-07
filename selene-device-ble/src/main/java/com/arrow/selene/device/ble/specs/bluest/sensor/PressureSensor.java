package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;

public class PressureSensor extends BleSensorAbstract<PressureSensorProperties, FloatSensorData> {
	private static final String PRESSURE = "pressure";
	private static final float PRESSURE_SCALE = 100.0f;
	private static final String UUID = BlueST.getUuid(Feature.PRESSURE);

	{
		payloadSize = 4; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data, int offset) {
		return Collections.singletonList(
		        new FloatSensorData(PRESSURE, BleUtils.thirtyTwoBitUnsignedAtOffset(data, offset) / PRESSURE_SCALE,
		                EngineConstants.FORMAT_DECIMAL_2));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
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
	protected PressureSensorProperties createProperties() {
		return new PressureSensorProperties();
	}
}
