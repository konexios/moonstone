package moonstone.selene.device.xbee.zcl.domain.general.alarms.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class AlarmClusterAttributes {
	public static final int ALARM_COUNT_ATTRIBUTE_ID = 0x0000;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ALARM_COUNT_ATTRIBUTE_ID, new ImmutablePair<>("Alarm Count", null));
	}
}
