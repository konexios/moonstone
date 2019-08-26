package moonstone.selene.device.xbee.zcl.domain.ha.statistics.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class ApplianceStatisticsClusterAttributes {
	public static final int LOG_MAX_SIZE_ATTRIBUTE_ID = 0x0000;
	public static final int LOG_QUEUE_MAX_SIZE_ATTRIBUTE_ID = 0x0001;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(LOG_MAX_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Log Max Size", null));
		ALL.put(LOG_QUEUE_MAX_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Log Queue Max Size", null));
	}
}
