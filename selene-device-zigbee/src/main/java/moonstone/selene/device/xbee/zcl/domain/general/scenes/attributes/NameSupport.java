package moonstone.selene.device.xbee.zcl.domain.general.scenes.attributes;

import moonstone.selene.device.sensor.BooleanSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class NameSupport implements Attribute<BooleanSensorData> {
	public static boolean isGroupNameSupported(byte... value) {
		return (value[0] & 0b1_0000000) != 0;
	}

	@Override
	public int getId() {
		return ScenesClusterAttributes.NAME_SUPPORT_ATTRIBUTE_ID;
	}

	@Override
	public BooleanSensorData toData(String name, byte... value) {
		return new BooleanSensorData(name, isGroupNameSupported(value));
	}
}
