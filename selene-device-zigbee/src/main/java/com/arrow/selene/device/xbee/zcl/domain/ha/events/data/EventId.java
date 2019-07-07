package com.arrow.selene.device.xbee.zcl.domain.ha.events.data;

public enum EventId {
	END_OF_CYCLE(0x01),
	TEMPERATURE_REACHED(0x04),
	END_OF_COOKING(0x05),
	SWITCHING_OFF(0x06),
	WRONG_DATA(0xf7);

	private final int value;

	EventId(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static EventId getByValue(int value) {
		for (EventId item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return null;
	}
}
