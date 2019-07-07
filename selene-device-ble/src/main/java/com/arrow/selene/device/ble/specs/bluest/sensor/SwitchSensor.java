package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.sensor.BooleanSensorData;

public class SwitchSensor extends BleSensorAbstract<SwitchSensorProperties, BooleanSensorData> {
	private static final String SWITCH = "led";
	private static final String UUID = BlueST.getUuid(Feature.SWITCH);

	{
		payloadSize = 1; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<BooleanSensorData> parseData(byte[] data, int offset) {
		return Collections.singletonList(new BooleanSensorData(SWITCH, (data[offset] > 0)));
	}

	@Override
	public List<BooleanSensorData> parseData(byte[] data) {
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
	protected SwitchSensorProperties createProperties() {
		return new SwitchSensorProperties();
	}
}
