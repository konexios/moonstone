package com.arrow.selene.device.xbee.zcl.domain.se.demand.data;

import java.util.EnumSet;
import java.util.Set;

public enum EventControl {
	RANDOMIZED_START_NOT_APPLIED((byte) 0, false),
	RANDOMIZE_START_TIME((byte) 0, true),
	RANDOMIZED_END_NOT_APPLIED((byte) 1, false),
	RANDOMIZE_END_TIME((byte) 1, true);

	private final byte bit;
	private final boolean value;

	EventControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<EventControl> getByValue(byte value) {
		Set<EventControl> result = EnumSet.noneOf(EventControl.class);
		for (EventControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<EventControl> items) {
		int result = 0;
		for (EventControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}
