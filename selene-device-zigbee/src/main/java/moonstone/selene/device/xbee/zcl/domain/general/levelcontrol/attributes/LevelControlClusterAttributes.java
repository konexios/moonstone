package moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class LevelControlClusterAttributes {
	public static final int CURRENT_LEVEL_ATTRIBUTE_ID = 0x0000;
	public static final int REMAINING_TIME_ATTRIBUTE_ID = 0x0001;
	public static final int ON_OFF_TRANSACTION_TIME_ATTRIBUTE_ID = 0x0010;
	public static final int ON_LEVEL_ATTRIBUTE_ID = 0x0011;
	public static final int ON_TRANSITION_TIME_ATTRIBUTE_ID = 0x0012;
	public static final int OFF_TRANSITION_TIME_ATTRIBUTE_ID = 0x0013;
	public static final int DEFAULT_MOVE_RATE_ATTRIBUTE_ID = 0x0014;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(CURRENT_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Current Level", null));
		ALL.put(REMAINING_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Remaining Time", new RemainingTime()));
		ALL.put(ON_OFF_TRANSACTION_TIME_ATTRIBUTE_ID,
				new ImmutablePair<>("On Off Transaction Time", new OnOffTransactionTime()));
		ALL.put(ON_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("On Level", null));
		ALL.put(ON_TRANSITION_TIME_ATTRIBUTE_ID, new ImmutablePair<>("On Transition Time", new OnTransitionTime()));
		ALL.put(OFF_TRANSITION_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Off Transition Time", new OffTransitionTime()));
		ALL.put(DEFAULT_MOVE_RATE_ATTRIBUTE_ID, new ImmutablePair<>("Default Move Rate", null));
	}
}
