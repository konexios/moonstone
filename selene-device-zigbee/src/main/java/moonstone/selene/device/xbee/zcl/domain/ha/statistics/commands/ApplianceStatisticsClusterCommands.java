package moonstone.selene.device.xbee.zcl.domain.ha.statistics.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ApplianceStatisticsClusterCommands {
	public static final int LOG_NOTIFICATION_COMMAND_ID = 0x00;
	public static final int LOG_RESPONSE_COMMAND_ID = 0x01;
	public static final int LOG_QUEUE_RESPONSE_COMMAND_ID = 0x02;
	public static final int STATISTICS_AVAILABLE_COMMAND_ID = 0x03;

	public static final int LOG_REQUEST_COMMAND_ID = 0x00;
	public static final int LOG_QUEUE_REQUEST_COMMAND_ID = 0x01;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(LOG_NOTIFICATION_COMMAND_ID, "Log Notification");
		ALL_GENERATED.put(LOG_RESPONSE_COMMAND_ID, "Log Response");
		ALL_GENERATED.put(LOG_QUEUE_RESPONSE_COMMAND_ID, "Log Queue Response");
		ALL_GENERATED.put(STATISTICS_AVAILABLE_COMMAND_ID, "Statistics Available");

		ALL_RECEIVED.put(LOG_REQUEST_COMMAND_ID, new ImmutablePair<>("Log Request", LogRequest.class));
		ALL_RECEIVED.put(LOG_QUEUE_REQUEST_COMMAND_ID, new ImmutablePair<>("Log Queue Request", LogQueueRequest
				.class));
	}
}
