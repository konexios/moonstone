package moonstone.selene.device.xbee.zcl.domain.protocol.iso7816.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class Iso7816ProtocolTunnelClusterCommands {
	public static final int INSERT_SMART_CARD_COMMAND_ID = 0x01;
	public static final int EXTRACT_SMART_CARD_COMMAND_ID = 0x02;

	// Either direction
	public static final int TRANSFER_APDU_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(TRANSFER_APDU_COMMAND_ID, new ImmutablePair<>("Transfer A PDU", NoPayloadCommand.class));
		ALL_RECEIVED.put(INSERT_SMART_CARD_COMMAND_ID,
				new ImmutablePair<>("Insert Smart Card", NoPayloadCommand.class));
		ALL_RECEIVED.put(EXTRACT_SMART_CARD_COMMAND_ID,
				new ImmutablePair<>("Extract Smart Card", NoPayloadCommand.class));

		ALL_GENERATED.put(TRANSFER_APDU_COMMAND_ID, "Disconnect Request");
	}
}
