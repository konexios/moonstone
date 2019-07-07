package com.arrow.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum CommandOptions {
	REQUESTOR_RX_ON_WHEN_IDLE;

	public static Set<CommandOptions> getByValue(int value) {
		Set<CommandOptions> result = EnumSet.noneOf(CommandOptions.class);
		for (CommandOptions item : values()) {
			if((value >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}
}
