package moonstone.selene.device.xbee.zcl.domain.general.onoff.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class OnOffClusterAttributes {
	public static final int ON_OFF_ATTRIBUTE_ID = 0x0000;

	public static final int GLOBAL_SCENE_CONTROL_ATTRIBUTE_ID = 0x4000;
	public static final int ON_TIME_ATTRIBUTE_ID = 0x4001;
	public static final int OFF_WAIT_TIME_ATTRIBUTE_ID = 0x4002;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ON_OFF_ATTRIBUTE_ID, new ImmutablePair<>("On/Off", null));
		ALL.put(GLOBAL_SCENE_CONTROL_ATTRIBUTE_ID, new ImmutablePair<>("Global Scene Control", null));
		ALL.put(ON_TIME_ATTRIBUTE_ID, new ImmutablePair<>("On Time", null));
		ALL.put(OFF_WAIT_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Off Wait Time", null));
	}
}
