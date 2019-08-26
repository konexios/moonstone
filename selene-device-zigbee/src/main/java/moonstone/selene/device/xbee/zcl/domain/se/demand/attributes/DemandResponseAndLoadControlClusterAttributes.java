package moonstone.selene.device.xbee.zcl.domain.se.demand.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class DemandResponseAndLoadControlClusterAttributes {
	public static final int UTILITY_ENROLLMENT_GROUP_ATTRIBUTE_ID = 0x0000;
	public static final int START_RANDOMIZE_MINUTES_ATTRIBUTE_ID = 0x0001;
	public static final int STOP_RANDOMIZE_MINUTES_ATTRIBUTE_ID = 0x0002;
	public static final int DEVICE_CLASS_VALUE_ATTRIBUTE_ID = 0x0003;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(UTILITY_ENROLLMENT_GROUP_ATTRIBUTE_ID, new ImmutablePair<>("Utility Enrollment Group", null));
		ALL.put(START_RANDOMIZE_MINUTES_ATTRIBUTE_ID, new ImmutablePair<>("Start Randomize Minutes", null));
		ALL.put(STOP_RANDOMIZE_MINUTES_ATTRIBUTE_ID, new ImmutablePair<>("Stop Randomize Minutes", null));
		ALL.put(DEVICE_CLASS_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Device Class Value", null));
	}
}
