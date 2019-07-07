package com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum DefaultConfigurationRegister implements Attribute<SetSensorData> {
	ENABLE_LOCAL_PROGRAMMING_ATTRIBUTE_DEFAULT_VALUE_0((byte) 0, false),
	ENABLE_LOCAL_PROGRAMMING_ATTRIBUTE_DEFAULT_VALUE_1((byte) 0, true),
	KEYPAD_INTERFACE_DEFAULT_ACCESS_DISABLED((byte) 1, false),
	KEYPAD_INTERFACE_DEFAULT_ACCESS_ENABLED((byte) 1, true),
	RF_INTERFACE_DEFAULT_ACCESS_DISABLED((byte) 2, false),
	RF_INTERFACE_DEFAULT_ACCESS_ENABLED((byte) 2, true),
	SOUND_VOLUME_ATTRIBUTE_DEFAULT_VALUE_0((byte) 5, false),
	SOUND_VOLUME_ATTRIBUTE_DEFAULT_VALUE_NOT_0((byte) 5, true),
	AUTO_RELOCK_TIME_ATTRIBUTE_DEFAULT_VALUE_0((byte) 6, false),
	AUTO_RELOCK_TIME_ATTRIBUTE_DEFAULT_VALUE_NOT_0((byte) 6, true),
	LED_SETTINGS_ATTRIBUTE_DEFAULT_VALUE_0((byte) 7, false),
	LED_SETTINGS_ATTRIBUTE_DEFAULT_VALUE_NOT_0((byte) 7, true);

	private final byte bit;
	private final boolean value;

	DefaultConfigurationRegister(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<DefaultConfigurationRegister> getByValue(byte... value) {
		Set<DefaultConfigurationRegister> result = EnumSet.noneOf(DefaultConfigurationRegister.class);
		for (DefaultConfigurationRegister item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.DEFAULT_CONFIGURATION_REGISTER_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
