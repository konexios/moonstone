package moonstone.selene.device.xbee.zcl.domain.lighting.color.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum DriftCompensation implements Attribute<StringSensorData> {
	NONE((byte) 0x00),
	OTHER_UNKNOWN((byte) 0x01),
	TEMPERATURE_MONITORING((byte) 0x02),
	OPTICAL_LUMINANCE_MONITORING_AND_FEEDBACK((byte) 0x03),
	OPTICAL_COLOR_MONITORING_AND_FEEDBACK((byte) 0x04),
	RESERVED((byte) 0x05, (byte) 0xff);

	private final byte min;
	private final byte max;

	DriftCompensation(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	DriftCompensation(byte value) {
		min = value;
		max = value;
	}

	public static DriftCompensation getByValue(byte... value) {
		for (DriftCompensation item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return LightingColorClusterAttributes.DRIFT_COMPENSATION_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
