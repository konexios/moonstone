package moonstone.selene.device.xbee.zcl.domain.se.demand.data;

public enum EventStatus {
	LOAD_CONTROL_EVENT_COMMAND_RECEIVED(0x01),
	EVENT_STARTED(0x02),
	EVENT_COMPLETED(0x03),
	USER_HAS_CHOSEN_TO_OPT_OUT(0x04),
	USER_HAS_CHOSEN_TO_OPT_IN(0x05),
	THE_EVENT_HAS_BEEN_CANCELLED(0x06),
	THE_EVENT_HAS_BEEN_SUPERSEDED(0x07),
	EVENT_PARTIALLY_COMPLETED_WITH_USER_OPT_OUT(0x08),
	EVENT_PARTIALLY_COMPLETED_DUE_TO_USER_OPT_IN(0x09),
	EVENT_COMPLETED_NO_USER_PARTICIPATION_PREVIOUS_OPT_OUT(0x0a),
	REJECTED_INVALID_CANCEL_COMMAND_DEFAULT(0xf8),
	REJECTED_INVALID_CANCEL_COMMAND_INVALID_EFFECTIVE_TIME_(0xf9),
	REJECTED_EVENT_WAS_RECEIVED_AFTER_IT_HAD_EXPIRED(0xfb),
	REJECTED_INVALID_CANCEL_COMMAND_UNDEFINED_EVENT(0xfd),
	LOAD_CONTROL_EVENT_COMMAND_REJECTED(0xfe);

	private final int value;

	EventStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static EventStatus getByValue(int value) {
		for (EventStatus item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return null;
	}
}
