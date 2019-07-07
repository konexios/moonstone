package com.arrow.selene.device.xbee.zcl.domain.general.identify.data;

import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public enum EzModeInvokeAction {
	FACTORY_FRESH,
	NETWORK_STEERING,
	FINDING_AND_BINDING;

	public static Set<EzModeInvokeAction> getByValue(int... value) {
		Validate.notNull(value, "value is null");
		Validate.isTrue(value.length > 0, "value length is incorrect");
		Set<EzModeInvokeAction> result = EnumSet.noneOf(EzModeInvokeAction.class);
		for (EzModeInvokeAction item : EzModeInvokeAction.values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(EzModeInvokeAction... actions) {
		Validate.notNull(actions, "actions is null");
		int result = 0;
		for (EzModeInvokeAction action : actions) {
			result |= 1 << action.ordinal();
		}
		return result;
	}
}
