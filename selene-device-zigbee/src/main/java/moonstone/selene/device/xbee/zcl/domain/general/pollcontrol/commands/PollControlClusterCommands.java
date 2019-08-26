package moonstone.selene.device.xbee.zcl.domain.general.pollcontrol.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PollControlClusterCommands {
	public static final int CHECK_IN_COMMAND_ID = 0x00;

	public static final int CHECK_IN_RESPONSE_COMMAND_ID = 0x00;
	public static final int FAST_POLL_STOP_COMMAND_ID = 0x01;
	public static final int SET_LONG_POLL_INTERVAL_COMMAND_ID = 0x02;
	public static final int SET_SHORT_POLL_INTERVAL_COMMAND_ID = 0x03;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(CHECK_IN_COMMAND_ID, "Check In");

		ALL_RECEIVED.put(CHECK_IN_RESPONSE_COMMAND_ID, new ImmutablePair<>("Check In Response", CheckInResponse
				.class));
		ALL_RECEIVED.put(FAST_POLL_STOP_COMMAND_ID, new ImmutablePair<>("Fast Poll Stop", FastPollStop.class));
		ALL_RECEIVED.put(SET_LONG_POLL_INTERVAL_COMMAND_ID,
				new ImmutablePair<>("Set Long Poll Interval", SetLongPollInterval.class));
		ALL_RECEIVED.put(SET_SHORT_POLL_INTERVAL_COMMAND_ID,
				new ImmutablePair<>("Set Short Poll Interval", SetShortPollInterval.class));
	}
}
