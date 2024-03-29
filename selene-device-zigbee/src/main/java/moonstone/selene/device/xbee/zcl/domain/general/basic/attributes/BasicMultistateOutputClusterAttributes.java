package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class BasicMultistateOutputClusterAttributes {
	public static final int STATE_TEXT_ATTRIBUTE_ID = 0x000e;
	public static final int DESCRIPTION_ATTRIBUTE_ID = 0x001c;
	public static final int NUMBER_OF_STATUSES_ATTRIBUTE_ID = 0x004a;
	public static final int OUT_OF_SERVICE_ATTRIBUTE_ID = 0x0051;
	public static final int PRESENT_VALUE_ATTRIBUTE_ID = 0x0055;
	public static final int PRIORITY_ARRAY_ATTRIBUTE_ID = 0x0057;
	public static final int RELIABILITY_ATTRIBUTE_ID = 0x0067;
	public static final int RELINQUISH_DEFAULT_ATTRIBUTE_ID = 0x0068;
	public static final int STATUS_FLAGS_ATTRIBUTE_ID = 0x006f;
	public static final int APPLICATION_TYPE_ATTRIBUTE_ID = 0x0100;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(STATE_TEXT_ATTRIBUTE_ID, new ImmutablePair<>("State Text", null));
		ALL.put(DESCRIPTION_ATTRIBUTE_ID, new ImmutablePair<>("Description", null));
		ALL.put(NUMBER_OF_STATUSES_ATTRIBUTE_ID, new ImmutablePair<>("Number of Statuses", null));
		ALL.put(OUT_OF_SERVICE_ATTRIBUTE_ID, new ImmutablePair<>("Out of Service", null));
		ALL.put(PRESENT_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Present Value", null));
		ALL.put(PRIORITY_ARRAY_ATTRIBUTE_ID, new ImmutablePair<>("Priority Array", null));
		ALL.put(RELIABILITY_ATTRIBUTE_ID, new ImmutablePair<>("Reliablity", null));
		ALL.put(RELINQUISH_DEFAULT_ATTRIBUTE_ID, new ImmutablePair<>("Relinquish Default", null));
		ALL.put(STATUS_FLAGS_ATTRIBUTE_ID, new ImmutablePair<>("Status Flags", null));
		ALL.put(APPLICATION_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Application Type", null));
	}
}
