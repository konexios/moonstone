package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class BasicAnalogValueClusterAttributes {
	public static final int DESCRIPTION_ATTRIBUTE_ID = 0x001c;
	public static final int OUT_OF_SERVICE_ATTRIBUTE_ID = 0x0051;
	public static final int PRESENT_VALUE_ATTRIBUTE_ID = 0x0055;
	public static final int PRIORITY_ARRAY_ATTRIBUTE_ID = 0x0057;
	public static final int RELIABILITY_ATTRIBUTE_ID = 0x0067;
	public static final int RELINQUISH_DEFAULT_ATTRIBUTE_ID = 0x0068;
	public static final int STATUS_FLAGS_ATTRIBUTE_ID = 0x006f;
	public static final int ENGINEERING_UNITS_ATTRIBUTE_ID = 0x0075;
	public static final int APPLICATION_TYPE_ATTRIBUTE_ID = 0x0100;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(DESCRIPTION_ATTRIBUTE_ID, new ImmutablePair<>("Description", null));
		ALL.put(OUT_OF_SERVICE_ATTRIBUTE_ID, new ImmutablePair<>("Out of Service", null));
		ALL.put(PRESENT_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Present Value", null));
		ALL.put(PRIORITY_ARRAY_ATTRIBUTE_ID, new ImmutablePair<>("Priority Array", null));
		ALL.put(RELIABILITY_ATTRIBUTE_ID, new ImmutablePair<>("Reliability", null));
		ALL.put(RELINQUISH_DEFAULT_ATTRIBUTE_ID, new ImmutablePair<>("Relinquish Default", null));
		ALL.put(STATUS_FLAGS_ATTRIBUTE_ID, new ImmutablePair<>("Status Flags", null));
		ALL.put(ENGINEERING_UNITS_ATTRIBUTE_ID, new ImmutablePair<>("Engineering Units", null));
		ALL.put(APPLICATION_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Application Type", null));
	}
}
