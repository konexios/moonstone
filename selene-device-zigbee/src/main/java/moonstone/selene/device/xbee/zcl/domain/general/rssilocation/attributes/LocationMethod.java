package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum LocationMethod implements Attribute<StringSensorData> {
	LATERATION((byte) 0x00),
	SIGNPOSTING((byte) 0x01),
	RF_FINGERPRINTING((byte) 0x02),
	OUT_OF_BAND((byte) 0x03),
	CENTRALIZED((byte) 0x04),
	RESERVED((byte) 0x05, (byte) 0x3f),
	RESERVED_FOR_MANUFACTURER_SPECIFIC((byte) 0x40, (byte) 0xff);

	private final byte min;
	private final byte max;

	LocationMethod(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	LocationMethod(byte value) {
		min = value;
		max = value;
	}

	public static LocationMethod getByValue(byte... value) {
		for (LocationMethod item : values()) {
			if (value[0] >= item.min && value[0] <= item.max) {
				return item;
			}
		}
		return null;
	}

	@Override
	public int getId() {
		return RssiLocationClusterAttributes.LOCATION_METHOD_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
