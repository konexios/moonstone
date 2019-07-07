package com.arrow.selene.device.xbee.zcl.domain.security.zone.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ZoneState implements Attribute<StringSensorData> {
	NOT_ENROLLED((byte) 0x00),
	ENROLLED((byte) 0x01),
	RESERVED((byte) 0x02, (byte) 0xff);

	private final byte min;
	private final byte max;

	ZoneState(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ZoneState(byte value) {
		min = value;
		max = value;
	}

	public static ZoneState getByValue(byte... value) {
		for (ZoneState item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return SecurityZoneClusterAttributes.ZONE_STATE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
