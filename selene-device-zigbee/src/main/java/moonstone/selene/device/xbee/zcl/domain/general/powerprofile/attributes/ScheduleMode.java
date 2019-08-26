package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ScheduleMode implements Attribute<SetSensorData> {
	SCHEDULE_MODE_CHEAPEST,
	SCHEDULE_MODE_GREENEST;

	public static Set<ScheduleMode> getByValue(byte... value) {
		Set<ScheduleMode> result = EnumSet.noneOf(ScheduleMode.class);
		for (ScheduleMode item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return PowerProfileClusterAttributes.SCHEDULE_MODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
