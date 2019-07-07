package com.arrow.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.sensor.IntegerSensorData;

public class CarryPositionSensor extends BleSensorAbstract<CarryPositionSensorProperties, IntegerSensorData> {
	private static final String CARRY_POSITION = "carryPosition";
	private static final String UUID = BlueST.getUuid(Feature.CARRY_POSITION);

	{
		payloadSize = 1;
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
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
	public List<IntegerSensorData> parseData(byte[] data, int offset) {
		return Collections
		        .singletonList(new IntegerSensorData(CARRY_POSITION, BleUtils.byteUnsignedAtOffset(data, offset)));
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
		// not implemented
		return null;
	}

	@Override
	protected CarryPositionSensorProperties createProperties() {
		return new CarryPositionSensorProperties();
	}
}
