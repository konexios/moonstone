package moonstone.selene.device.xbee.zcl.domain.protocol.generic.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GenericTunnelClusterCommands {
	public static final int MATCH_PROTOCOL_ADDRESS_RESPONSE_COMMAND_ID = 0x00;
	public static final int ADVERTISE_PROTOCOL_ADDRESS_COMMAND_ID = 0x01;

	public static final int MATCH_PROTOCOL_ADDRESS_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(MATCH_PROTOCOL_ADDRESS_RESPONSE_COMMAND_ID, "Match Protocol Address Response");
		ALL_GENERATED.put(ADVERTISE_PROTOCOL_ADDRESS_COMMAND_ID, "Advertise Protocol Address");

		ALL_RECEIVED.put(MATCH_PROTOCOL_ADDRESS_COMMAND_ID,
				new ImmutablePair<>("Match Protocol Address", MatchProtocolAddress.class));
	}
}
