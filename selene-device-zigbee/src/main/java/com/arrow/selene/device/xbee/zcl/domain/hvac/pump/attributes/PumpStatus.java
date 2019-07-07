package com.arrow.selene.device.xbee.zcl.domain.hvac.pump.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum PumpStatus implements Attribute<SetSensorData> {
	DEVICE_FAULT,
	SUPPLY_FAULT,
	SPEED_LOW,
	SPEED_HIGH,
	LOCAL_OVERRIDE,
	RUNNING,
	REMOTE_PRESSURE,
	REMOTE_FLOW,
	REMOTE_TEMPERATURE;

	public static Set<PumpStatus> getByValue(short value) {
		Set<PumpStatus> result = EnumSet.noneOf(PumpStatus.class);
		for (PumpStatus pumpStatus : values()) {
			if ((value >> pumpStatus.ordinal() & 0x01) == 1) {
				result.add(pumpStatus);
			}
		}
		return result;
	}

	public static Set<PumpStatus> getByValue(byte... value) {
		return getByValue(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return HvacPumpClusterAttributes.PUMP_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
