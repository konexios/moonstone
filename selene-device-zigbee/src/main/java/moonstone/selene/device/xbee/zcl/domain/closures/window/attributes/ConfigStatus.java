package moonstone.selene.device.xbee.zcl.domain.closures.window.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ConfigStatus implements Attribute<SetSensorData> {
	WINDOW_COVERING_NOT_OPERATIONAL((byte) 0, false),
	WINDOW_COVERING_OPERATIONAL((byte) 0, true),
	WINDOW_COVERING_NOT_ONLINE((byte) 1, false),
	WINDOW_COVERING_ONLINE((byte) 1, true),
	DIRECTION_NORMAL((byte) 2, false),
	DIRECTION_REVERSED((byte) 2, true),
	LIFT_CONTROL_IS_OPEN_LOOP((byte) 3, false),
	LIFT_CONTROL_IS_CLOSED_LOOP((byte) 3, true),
	TILT_CONTROL_IS_OPEN_LOOP((byte) 4, false),
	TILT_CONTROL_IS_CLOSED_LOOP((byte) 4, true),
	LIFT_CLOSED_LOOP_TIMER_CONTROLLED((byte) 5, false),
	LIFT_CLOSED_LOOP_ENCODER_CONTROLLED((byte) 5, true),
	TILT_CLOSED_LOOP_TIMER_CONTROLLED((byte) 6, false),
	TILT_CLOSED_LOOP_ENCODER_CONTROLLED((byte) 6, true);

	private final byte bit;
	private final boolean value;

	ConfigStatus(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<ConfigStatus> getByValue(byte... value) {
		Set<ConfigStatus> result = EnumSet.noneOf(ConfigStatus.class);
		for (ConfigStatus item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return WindowCoveringClusterAttributes.CONFIG_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
