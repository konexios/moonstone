package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.sensor.IntegerSensorData;

public class LightSensor extends BleSensorAbstract<LightSensorProperties, IntegerSensorData> {
	private static final String LIGHT = "light";
	private static final String UUID = BlueST.getUuid(Feature.LIGHT);

	{
		payloadSize = 2; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data, int offset) {
		return Collections.singletonList(new IntegerSensorData(LIGHT, BleUtils.shortSignedAtOffset(data, offset)));
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
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
	protected LightSensorProperties createProperties() {
		return new LightSensorProperties();
	}
}
