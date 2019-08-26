package moonstone.selene.device.xbee.zcl.domain.closures.window.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum Mode implements Attribute<SetSensorData> {
	MOTOR_DIRECTION_NORMAL((byte) 0, false),
	MOTOR_DIRECTION_REVERSED((byte) 0, true),
	RUN_IN_NORMAL_MODE((byte) 1, false),
	RUN_IN_CALIBRATION_MODE((byte) 1, true),
	MOTOR_RUNNING_NORMALLY((byte) 2, false),
	MOTOR_RUNNING_IN_MAINTENANCE_MODE((byte) 2, true),
	LEDS_OFF((byte) 3, false),
	LEDS_DISPLAY_FEEDBACK((byte) 3, true);

	private final byte bit;
	private final boolean value;

	Mode(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<Mode> getByValue(byte... value) {
		Set<Mode> result = EnumSet.noneOf(Mode.class);
		for (Mode item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return WindowCoveringClusterAttributes.MODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
