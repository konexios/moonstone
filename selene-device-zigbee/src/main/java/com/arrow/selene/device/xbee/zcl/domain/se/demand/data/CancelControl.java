package com.arrow.selene.device.xbee.zcl.domain.se.demand.data;

import java.util.EnumSet;
import java.util.Set;

public enum CancelControl {
	RANDOMIZATION_OVERRIDDEN_EVENT_SHOULD_BE_TERMINATED_IMMEDIATELY((byte) 0, false),
	EVENT_SHOULD_END_USING_RANDOMIZATION_SETTINGS_IN_ORIGINAL_EVENT((byte) 0, true);

	private final byte bit;
	private final boolean value;

	CancelControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<CancelControl> getByValue(byte value) {
		Set<CancelControl> result = EnumSet.noneOf(CancelControl.class);
		for (CancelControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<CancelControl> items) {
		int result = 0;
		for (CancelControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}
