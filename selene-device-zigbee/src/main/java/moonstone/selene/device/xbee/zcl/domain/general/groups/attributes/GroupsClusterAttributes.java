package moonstone.selene.device.xbee.zcl.domain.general.groups.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class GroupsClusterAttributes {
	public static final int NAME_SUPPORT_ATTRIBUTE_ID = 0x0000;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(NAME_SUPPORT_ATTRIBUTE_ID, new ImmutablePair<>("Name Support", new NameSupport()));
	}
}
