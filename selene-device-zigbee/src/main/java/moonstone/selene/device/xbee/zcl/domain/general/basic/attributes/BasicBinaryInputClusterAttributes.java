package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class BasicBinaryInputClusterAttributes {
	public static final int ACTIVE_TEXT_ATTRIBUTE_ID = 0x0004;
	public static final int DESCRIPTION_ATTRIBUTE_ID = 0x001c;
	public static final int INACTIVE_TEXT_ATTRIBUTE_ID = 0x002e;
	public static final int OUT_OF_SERVICE_ATTRIBUTE_ID = 0x0051;
	public static final int POLARITY_ATTRIBUTE_ID = 0x0054;
	public static final int PRESENT_VALUE_ATTRIBUTE_ID = 0x0055;
	public static final int RELIABILITY_ATTRIBUTE_ID = 0x0067;
	public static final int STATUS_FLAGS_ATTRIBUTE_ID = 0x006f;
	public static final int APPLICATION_TYPE_ATTRIBUTE_ID = 0x0100;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ACTIVE_TEXT_ATTRIBUTE_ID, new ImmutablePair<>("Active Text", null));
		ALL.put(DESCRIPTION_ATTRIBUTE_ID, new ImmutablePair<>("Description", null));
		ALL.put(INACTIVE_TEXT_ATTRIBUTE_ID, new ImmutablePair<>("Inactive Text", null));
		ALL.put(OUT_OF_SERVICE_ATTRIBUTE_ID, new ImmutablePair<>("Out of Service", null));
		ALL.put(POLARITY_ATTRIBUTE_ID, new ImmutablePair<>("Polarity", null));
		ALL.put(PRESENT_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Present Value", null));
		ALL.put(RELIABILITY_ATTRIBUTE_ID, new ImmutablePair<>("Reliability", null));
		ALL.put(STATUS_FLAGS_ATTRIBUTE_ID, new ImmutablePair<>("Status Flags", null));
		ALL.put(APPLICATION_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Application Type", null));
	}
}
