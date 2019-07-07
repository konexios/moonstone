package com.arrow.selene.device.xbee.zcl.domain.general.time.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class TimeClusterAttributes {
	public final static int TIME_ATTRIBUTE_ID = 0x0000;
	public final static int TIME_STATUS_ATTRIBUTE_ID = 0x0001;
	public final static int TIME_ZONE_ATTRIBUTE_ID = 0x0002;
	public final static int DST_START_ATTRIBUTE_ID = 0x0003;
	public final static int DST_END_ATTRIBUTE_ID = 0x0004;
	public final static int DST_SHIFT_ATTRIBUTE_ID = 0x0005;
	public final static int STANDARD_TIME_ATTRIBUTE_ID = 0x0006;
	public final static int LOCAL_TIME_ATTRIBUTE_ID = 0x0007;
	public final static int LAST_SET_TIME_ATTRIBUTE_ID = 0x0008;
	public final static int VALID_UNTIL_TIME_ATTRIBUTE_ID = 0x0009;

	public final static Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(TIME_ATTRIBUTE_ID, new ImmutablePair<>("Time", null));
		ALL.put(TIME_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Time Status", TimeStatus.MASTER_CLOCK));
		ALL.put(TIME_ZONE_ATTRIBUTE_ID, new ImmutablePair<>("Timezone", null));
		ALL.put(DST_START_ATTRIBUTE_ID, new ImmutablePair<>("DST Start", null));
		ALL.put(DST_END_ATTRIBUTE_ID, new ImmutablePair<>("DST End", null));
		ALL.put(DST_SHIFT_ATTRIBUTE_ID, new ImmutablePair<>("DST Shift", null));
		ALL.put(STANDARD_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Standard Time", null));
		ALL.put(LOCAL_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Local Time", null));
		ALL.put(LAST_SET_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Last Set Time", null));
		ALL.put(VALID_UNTIL_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Valid Until Time", null));
	}
}
