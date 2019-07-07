package com.arrow.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum CurrencyChangeControl {
	DO_NOT_CLEAR_BILLING_INFORMATION((byte) 0, false),
	CLEAR_BILLING_INFORMATION((byte) 0, true),
	DO_NOT_CONVERT_BILLING_INFORMATION((byte) 1, false),
	CONVERT_BILLING_INFORMATION((byte) 1, true),
	DO_NOT_CLEAR_OLD_CONSUMPTION_DATA((byte) 2, false),
	CLEAR_OLD_CONSUMPTION_DATA((byte) 2, true),
	DO_NOT_CONVERT_OLD_CONSUMPTION_DATA((byte) 3, false),
	CONVERT_OLD_CONSUMPTION_DATA((byte) 3, true);

	private final byte bit;
	private final boolean value;

	CurrencyChangeControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<CurrencyChangeControl> getByValue(byte value) {
		Set<CurrencyChangeControl> result = EnumSet.noneOf(CurrencyChangeControl.class);
		for (CurrencyChangeControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<CurrencyChangeControl> items) {
		int result = 0;
		for (CurrencyChangeControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}
