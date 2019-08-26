package moonstone.selene.device.xbee.zcl.domain.general.identify.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class IdentifyClusterCommands {
	public static final int IDENTIFY_COMMAND_ID = 0x00;
	public static final int IDENTIFY_QUERY_COMMAND_ID = 0x01;
	public static final int EZ_MODE_INVOKE_COMMAND_ID = 0x02;
	public static final int UPDATE_COMMISSION_STATE_COMMAND_ID = 0x03;
	public static final int TRIGGER_EFFECT_COMMAND_ID = 0x40;

	public static final int IDENTIFY_QUERY_RESPONSE_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(IDENTIFY_COMMAND_ID, new ImmutablePair<>("Identify", Identify.class));
		ALL_RECEIVED.put(IDENTIFY_QUERY_COMMAND_ID, new ImmutablePair<>("Identify Query", IdentifyQuery.class));
		ALL_RECEIVED.put(EZ_MODE_INVOKE_COMMAND_ID, new ImmutablePair<>("EZ-Mode Invoke", EzModeInvoke.class));
		ALL_RECEIVED.put(UPDATE_COMMISSION_STATE_COMMAND_ID,
				new ImmutablePair<>("Update Commission State", UpdateCommissionState.class));
		ALL_RECEIVED.put(TRIGGER_EFFECT_COMMAND_ID, new ImmutablePair<>("Trigger Effect", NoPayloadCommand.class));

		ALL_GENERATED.put(IDENTIFY_QUERY_RESPONSE_COMMAND_ID, "Identify Query Response");
	}
}
