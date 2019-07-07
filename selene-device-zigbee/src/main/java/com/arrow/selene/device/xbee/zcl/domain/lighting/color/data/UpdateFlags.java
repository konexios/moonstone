package com.arrow.selene.device.xbee.zcl.domain.lighting.color.data;

import java.util.EnumSet;
import java.util.Set;

public enum UpdateFlags {
	ACTION,
	DIRECTION,
	TIME,
	START_HUE;

	public static Set<UpdateFlags> getByValue(byte value) {
		Set<UpdateFlags> result = EnumSet.noneOf(UpdateFlags.class);
		for (UpdateFlags item : values()) {
			if((value >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}
}
