package moonstone.selene.device.xbee.zcl.domain.general.commissioning.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class CommissioningClusterCommands {
	public static final int RESTART_DEVICE_COMMAND_ID = 0x00;
	public static final int SAVE_STARTUP_PARAMETERS_COMMAND_ID = 0x01;
	public static final int RESTORE_STARTUP_PARAMETERS_COMMAND_ID = 0x02;
	public static final int RESET_STARTUP_PARAMETERS_COMMAND_ID = 0x03;

	public static final int RESTART_DEVICE_RESPONSE_COMMAND_ID = 0x00;
	public static final int SAVE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID = 0x01;
	public static final int RESTORE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID = 0x02;
	public static final int RESET_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID = 0x03;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(RESTART_DEVICE_COMMAND_ID, new ImmutablePair<>("Restart Device", RestartDevice.class));
		ALL_RECEIVED.put(SAVE_STARTUP_PARAMETERS_COMMAND_ID,
				new ImmutablePair<>("Save Startup Parameters", SaveStartupParameters.class));
		ALL_RECEIVED.put(RESTORE_STARTUP_PARAMETERS_COMMAND_ID,
				new ImmutablePair<>("Restore Startup Parameters", ResetStartupParameters.class));
		ALL_RECEIVED.put(RESET_STARTUP_PARAMETERS_COMMAND_ID,
				new ImmutablePair<>("Reset Startup Parameters", ResetStartupParameters.class));

		ALL_GENERATED.put(RESTART_DEVICE_RESPONSE_COMMAND_ID, "Restart Device Response");
		ALL_GENERATED.put(SAVE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID, "Save Startup Parameters Response");
		ALL_GENERATED.put(RESTORE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID, "Restore Startup Parameters Response");
		ALL_GENERATED.put(RESET_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID, "Reset Startup Parameters Response");
	}
}
