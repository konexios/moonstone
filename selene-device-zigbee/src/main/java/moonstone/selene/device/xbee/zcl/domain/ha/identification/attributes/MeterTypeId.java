package moonstone.selene.device.xbee.zcl.domain.ha.identification.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum MeterTypeId implements Attribute<StringSensorData> {
	UTILITY_PRIMARY_METER(0x0000),
	UTILITY_PRODUCTION_METER(0x0001),
	UTILITY_SECONDARY_METER(0x0002),
	PRIVATE_PRIMARY_METER(0x0100),
	PRIVATE_PRODUCTION_METER(0x0101),
	PRIVATE_SECONDARY_METERS(0x0102),
	GENERIC_METER(0x0110);

	private final int value;

	MeterTypeId(int value) {
		this.value = value;
	}

	@Override
	public int getId() {
		return MeterIdentificationClusterAttributes.METER_TYPE_ID_ATTRIBUTE_ID;
	}

	public static MeterTypeId getByValue(byte... value) {
		int intValue = Byte.toUnsignedInt(value[0]);
		for (MeterTypeId item : values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return null;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}

