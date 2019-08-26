package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum AlarmMask implements Attribute<SetSensorData> {
	GENERAL_HARDWARE_FAULT,
	GENERAL_SOFTWARE_FAULT;

	public static Set<AlarmMask> getByValue(byte... value) {
		Set<AlarmMask> result = EnumSet.noneOf(AlarmMask.class);
		for (AlarmMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return BasicClusterAttributes.ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
