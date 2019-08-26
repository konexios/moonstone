package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum LocationType implements Attribute<SetSensorData> {
	MEASURED_LOCATION((byte) 0, false),
	ABSOLUTE_LOCATION((byte) 0, true),
	THREE_DIMENSIONAL((byte) 1, false),
	TWO_DIMENSIONAL((byte) 1, true),
	RECTANGULAR((byte) 2, false);

	private final byte bit;
	private final boolean value;

	LocationType(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<LocationType> getByValue(byte... value) {
		Set<LocationType> result = EnumSet.noneOf(LocationType.class);
		for (LocationType item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return RssiLocationClusterAttributes.LOCATION_TYPE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
