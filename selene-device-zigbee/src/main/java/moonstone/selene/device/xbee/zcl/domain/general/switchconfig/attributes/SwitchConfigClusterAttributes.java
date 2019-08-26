package moonstone.selene.device.xbee.zcl.domain.general.switchconfig.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class SwitchConfigClusterAttributes {
	public static final int SWITCH_TYPE_ATTRIBUTE_ID = 0x0000;
	public static final int SWITCH_ACTIONS_ATTRIBUTE_ID = 0x0010;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(SWITCH_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Switch Type", SwitchActions.RESERVED));
		ALL.put(SWITCH_ACTIONS_ATTRIBUTE_ID, new ImmutablePair<>("Switch Actions", SwitchType.RESERVED));
	}
}
