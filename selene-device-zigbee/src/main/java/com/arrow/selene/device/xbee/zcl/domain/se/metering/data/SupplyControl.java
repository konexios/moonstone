package com.arrow.selene.device.xbee.zcl.domain.se.metering.data;

import java.util.EnumSet;
import java.util.Set;

public enum SupplyControl {
	ACKNOWLEDGE_REQUIRED;

	public static Set<SupplyControl> getByValue(int value) {
		Set<SupplyControl> result = EnumSet.noneOf(SupplyControl.class);
		for (SupplyControl item : values()) {
			if ((value >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getByValue(Set<SupplyControl> items) {
		int result = 0;
		for (SupplyControl item : items) {
			result |= 1 << item.ordinal();
		}
		return result;
	}
}
