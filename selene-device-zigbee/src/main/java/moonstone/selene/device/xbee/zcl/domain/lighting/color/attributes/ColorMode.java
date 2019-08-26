package moonstone.selene.device.xbee.zcl.domain.lighting.color.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ColorMode implements Attribute<StringSensorData> {
	CURRENT_HUE_AND_CURRENT_SATURATION((byte) 0x00),
	CURRENT_X_AND_CURRENT_Y((byte) 0x01),
	COLOR_TEMPERATURE((byte) 0x02),
	RESERVED((byte) 0x03, (byte) 0xff);

	private final byte min;
	private final byte max;

	ColorMode(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	ColorMode(byte value) {
		min = value;
		max = value;
	}

	public static ColorMode getByValue(byte... value) {
		for (ColorMode item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return LightingColorClusterAttributes.COLOR_MODE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
