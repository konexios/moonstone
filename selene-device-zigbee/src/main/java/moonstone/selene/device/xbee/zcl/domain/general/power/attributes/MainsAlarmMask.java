package moonstone.selene.device.xbee.zcl.domain.general.power.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum MainsAlarmMask implements Attribute<SetSensorData> {
	MAINS_VOLTAGE_TOO_LOW,
	MAINS_VOLTAGE_TOO_HIGH;

	public static Set<MainsAlarmMask> getByValue(byte... value) {
		Set<MainsAlarmMask> result = EnumSet.noneOf(MainsAlarmMask.class);
		for (MainsAlarmMask alarmMask : values()) {
			if ((value[0] >> alarmMask.ordinal() & 0x01) == 1) {
				result.add(alarmMask);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return PowerConfigClusterAttributes.MAINS_ALARM_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
