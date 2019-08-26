package moonstone.selene.device.xbee.zcl.domain.general.appliance.data;

public enum RemoteEnableFlags {
	DISABLED(0x00),
	ENABLED_REMOTE_AND_ENERGY_CONTROL(0x01),
	TEMPORARILY_LOCKED_DISABLED(0x07),
	ENABLED_REMOTE_CONTROL(0x0f);

	private final int value;

	RemoteEnableFlags(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RemoteEnableFlags getByValue(int value) {
		for (RemoteEnableFlags item : values()) {
			if (item.value == value) {
				return item;
			}
		}
		return null;
	}
}
