package moonstone.selene.device.xbee.zcl.domain.general.onoff.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class OnOffClusterCommands {
	public static final int OFF_COMMAND_ID = 0x00;
	public static final int ON_COMMAND_ID = 0x01;
	public static final int TOGGLE_COMMAND_ID = 0x02;
	public static final int OFF_WITH_EFFECT_COMMAND_ID = 0x40;
	public static final int ON_WITH_RECALL_GLOBAL_SCENE_COMMAND_ID = 0x41;
	public static final int ON_WITH_TIMED_OFF_COMMAND_ID = 0x42;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(OFF_COMMAND_ID, new ImmutablePair<>("Off", Off.class));
		ALL_RECEIVED.put(ON_COMMAND_ID, new ImmutablePair<>("On", On.class));
		ALL_RECEIVED.put(TOGGLE_COMMAND_ID, new ImmutablePair<>("Toggle", Toggle.class));
		ALL_RECEIVED.put(OFF_WITH_EFFECT_COMMAND_ID, new ImmutablePair<>("Off With Effect", OffWithEffect.class));
		ALL_RECEIVED.put(ON_WITH_RECALL_GLOBAL_SCENE_COMMAND_ID,
				new ImmutablePair<>("On With Recall Global Scene", OnWithRecallGlobalScene.class));
		ALL_RECEIVED.put(ON_WITH_TIMED_OFF_COMMAND_ID, new ImmutablePair<>("On With Timed Off", OnWithTimedOff.class));
	}
}
