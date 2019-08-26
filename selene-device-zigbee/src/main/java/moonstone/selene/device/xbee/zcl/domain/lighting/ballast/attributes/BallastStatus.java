package moonstone.selene.device.xbee.zcl.domain.lighting.ballast.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum BallastStatus implements Attribute<SetSensorData> {
	BALLAST_FULLY_OPERATIONAL((byte) 0, false),
	BALLAST_NOT_FULLY_OPERATIONAL((byte) 0, true),
	ALL_LAMPS_IN_SOCKETS((byte) 1, false),
	NOT_ALL_LAMPS_IN_SOCKETS((byte) 1, true);

	private final byte bit;
	private final boolean value;

	BallastStatus(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<BallastStatus> getByValue(byte... value) {
		Set<BallastStatus> result = EnumSet.noneOf(BallastStatus.class);
		for (BallastStatus item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return LightingBallastClusterAttributes.BALLAST_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
