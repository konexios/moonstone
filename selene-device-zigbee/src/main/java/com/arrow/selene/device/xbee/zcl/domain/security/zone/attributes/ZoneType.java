package com.arrow.selene.device.xbee.zcl.domain.security.zone.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum ZoneType implements Attribute<StringSensorData> {
	STANDARD_SIE((short) 0x0000),
	RESERVED_0X0001_0X000C((short) 0x0001, (short) 0x000c),
	MOTION_SENSOR((short) 0x000d),
	RESERVED_0X000E_0X0014((short) 0x000e, (short) 0x0014),
	CONTACT_SWITCH((short) 0x0015),
	RESERVED_0X0016_0X0027((short) 0x0016, (short) 0x0027),
	FIRE_SENSOR((short) 0x0028),
	RESERVED_0X0029((short) 0x0029),
	WATER_SENSOR((short) 0x002a),
	GAS_SENSOR((short) 0x002b),
	PERSONAL_EMERGENCY_DEVICE((short) 0x002c),
	MOVEMENT_SENSOR((short) 0x002d),
	RESERVED_0X002E_0X010E((short) 0x002e, (short) 0x010e),
	REMOTE_CONTROL((short) 0x010f),
	RESERVED_0X0110_0X0114((short) 0x0110, (short) 0x0114),
	KEY_FOB((short) 0x0115),
	RESERVED_0X0116_0X021C((short) 0x0116, (short) 0x021c),
	KEYPAD((short) 0x021d),
	RESERVED_0X021E_0X0224((short) 0x021e, (short) 0x0224),
	STANDARD_WARNING_DEVICE((short) 0x0225),
	RESERVED_0X0226_0X7FFF((short) 0x0226, (short) 0x7fff),
	RESERVED_FOR_MANUFACTURER_SPECIFIC((short) 0x8000, (short) 0xfffe),
	INVALID((short) 0xffff);

	private final short min;
	private final short max;

	ZoneType(short min, short max) {
		this.min = min;
		this.max = max;
	}

	ZoneType(short value) {
		min = value;
		max = value;
	}

	public static ZoneType getByValue(short value) {
		for (ZoneType item : values()) {
			if (value >= item.min && value <= item.max) {
				return item;
			}
		}
		return null;
	}

	public static ZoneType getByValue(byte... value) {
		return getByValue(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return SecurityZoneClusterAttributes.ZONE_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
