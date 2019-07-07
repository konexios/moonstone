package com.arrow.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum PriceControl {
	PRICE_ACKNOWLEDGEMENT_NOT_REQUIRED((byte) 0, false),
	PRICE_ACKNOWLEDGEMENT_REQUIRED((byte) 0, true),
	TOTAL_TIERS_DOES_NOT_EXCEED_15((byte) 1, false),
	TOTAL_TIERS_EXCEEDS_THE_15_SPECIFIED_IN_THE_COMMAND((byte) 1, true);

	private final byte bit;
	private final boolean value;

	PriceControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<PriceControl> getByValue(byte value) {
		Set<PriceControl> result = EnumSet.noneOf(PriceControl.class);
		for (PriceControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<PriceControl> items) {
		int result = 0;
		for (PriceControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}
