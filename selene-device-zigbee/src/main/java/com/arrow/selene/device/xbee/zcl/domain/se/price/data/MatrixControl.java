package com.arrow.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum MatrixControl {
	BLOCK_ONLY_OR_BLOCK_TOU_BASE((byte) 0, false),
	TOU_BASE((byte) 0, true);

	private final byte bit;
	private final boolean value;

	MatrixControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<MatrixControl> getByValue(byte value) {
		Set<MatrixControl> result = EnumSet.noneOf(MatrixControl.class);
		for (MatrixControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<MatrixControl> items) {
		int result = 0;
		for (MatrixControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}
