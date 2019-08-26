package moonstone.selene.device.xbee.zcl.domain.general.identify.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class IdentifyClusterAttributes {
	public static final int IDENTIFY_TIME_ATTRIBUTE_ID = 0x0000;
	public static final int COMMISSION_STATE_ATTRIBUTE_ID = 0x0001;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(IDENTIFY_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Identify Time", null));
		ALL.put(COMMISSION_STATE_ATTRIBUTE_ID,
				new ImmutablePair<>("Commission State", CommissionState.DEVICE_IN_NETWORK));
	}
}
