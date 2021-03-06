package moonstone.selene.device.xbee.zcl.domain.protocol.bacnet.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class BacnetProtocolTunnelClusterCommands {
	public static final int TRANSFER_NPDU_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(TRANSFER_NPDU_COMMAND_ID, new ImmutablePair<>("Transfer NPDU", TransferNpdu.class));
	}
}
