package moonstone.selene.device.xbee.zcl.domain.general.identify.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum CommissionState implements Attribute<SetSensorData> {
	DEVICE_NOT_IN_NETWORK(0, false),
	DEVICE_IN_NETWORK(0, true),
	DEVICE_NOT_COMMISSIONED_FOR_OPERATION(1, false),
	DEVICE_COMMISSIONED_FOR_OPERATION(1, true);

	private final int bit;
	private final boolean value;

	CommissionState(int bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public int getBit() {
		return bit;
	}

	public boolean getValue() {
		return value;
	}

	public static Set<CommissionState> getByValue(byte... value) {
		Set<CommissionState> result = EnumSet.noneOf(CommissionState.class);
		for (CommissionState item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getByValues(Set<CommissionState> items) {
		int result = 0;
		for (CommissionState item : items) {
			if (item.getValue()) {
				result |= 1 << item.getBit();
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return IdentifyClusterAttributes.COMMISSION_STATE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
