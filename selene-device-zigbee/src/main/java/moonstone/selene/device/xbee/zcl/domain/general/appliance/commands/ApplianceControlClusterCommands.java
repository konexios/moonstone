package moonstone.selene.device.xbee.zcl.domain.general.appliance.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ApplianceControlClusterCommands {
	public static final int SIGNAL_STATE_RESPONSE_COMMAND_ID = 0x00;
	public static final int SIGNAL_STATE_NOTIFICATION_COMMAND_ID = 0x01;

	public static final int EXECUTION_OF_A_COMMAND_COMMAND_ID = 0x00;
	public static final int SIGNAL_STATE_COMMAND_ID = 0x01;
	public static final int WRITE_FUNCTIONS_COMMAND_ID = 0x02;
	public static final int OVERLOAD_PAUSE_RESUME_COMMAND_ID = 0x03;
	public static final int OVERLOAD_PAUSE_COMMAND_ID = 0x04;
	public static final int OVERLOAD_WARNING_COMMAND_ID = 0x05;
	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(SIGNAL_STATE_RESPONSE_COMMAND_ID, "Signal State Response");
		ALL_GENERATED.put(SIGNAL_STATE_NOTIFICATION_COMMAND_ID, "Signal State Notification");

		ALL_RECEIVED.put(EXECUTION_OF_A_COMMAND_COMMAND_ID,
				new ImmutablePair<>("Execution Of A Command", ExecutionOfACommand.class));
		ALL_RECEIVED.put(SIGNAL_STATE_COMMAND_ID, new ImmutablePair<>("Signal State", SignalState.class));
		ALL_RECEIVED.put(WRITE_FUNCTIONS_COMMAND_ID, new ImmutablePair<>("Write Functions", WriteFunctions.class));
		ALL_RECEIVED.put(OVERLOAD_PAUSE_RESUME_COMMAND_ID,
				new ImmutablePair<>("Overload Pause Resume", OverloadPauseResume.class));
		ALL_RECEIVED.put(OVERLOAD_PAUSE_COMMAND_ID, new ImmutablePair<>("Overload Pause", OverloadPause.class));
		ALL_RECEIVED.put(OVERLOAD_WARNING_COMMAND_ID, new ImmutablePair<>("Overload Warning", OverloadWarning.class));
	}
}
