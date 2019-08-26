package moonstone.selene.device.xbee.zcl.domain.closures.shade.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum Status implements Attribute<SetSensorData> {
	SHADE_NOT_OPERATIONAL((byte) 0, false),
	SHADE_OPERATIONAL((byte) 0, true),
	SHADE_NOT_ADJUSTING((byte) 1, false),
	SHADE_ADJUSTING((byte) 1, true),
	SHADE_CLOSING((byte) 2, false),
	SHADE_OPENING((byte) 2, true),
	MOTOR_CLOSING((byte) 3, false),
	MOTOR_OPENING((byte) 3, true);

	private final byte bit;
	private final boolean value;

	Status(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<Status> getByValue(byte... value) {
		Set<Status> result = EnumSet.noneOf(Status.class);
		for (Status item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return ShadeClusterAttributes.STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
