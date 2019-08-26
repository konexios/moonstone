package moonstone.selene.device.xbee.zcl.domain.closures.window.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum WindowCoveringType implements Attribute<StringSensorData> {
	ROLLERSHADE,
	ROLLERSHADE_2_MOTOR,
	ROLLERSHADE_EXTERIOR,
	ROLLERSHADE_EXTERIOR_2_MOTOR,
	DRAPERY,
	AWNING,
	SHUTTER,
	TILT_BLIND_TILT_ONLY,
	TILT_BLIND_LIFT_AND_TILT,
	PROJECTOR_SCREEN;

	public static WindowCoveringType getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return WindowCoveringClusterAttributes.WINDOW_COVERING_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
