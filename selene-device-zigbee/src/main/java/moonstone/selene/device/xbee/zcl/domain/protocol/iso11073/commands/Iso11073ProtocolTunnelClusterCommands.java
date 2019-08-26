package moonstone.selene.device.xbee.zcl.domain.protocol.iso11073.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class Iso11073ProtocolTunnelClusterCommands {
	public static final int TRANSFER_A_PDU_COMMAND_ID = 0x00;
	public static final int CONNECT_REQUEST_COMMAND_ID = 0x01;
	public static final int DISCONNECT_REQUEST_COMMAND_ID = 0x02;
	public static final int CONNECT_STATUS_NOTIFICATION_COMMAND_ID = 0x03;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(TRANSFER_A_PDU_COMMAND_ID, new ImmutablePair<>("Transfer A PDU", NoPayloadCommand.class));
		ALL_RECEIVED.put(CONNECT_REQUEST_COMMAND_ID, new ImmutablePair<>("Connect Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(DISCONNECT_REQUEST_COMMAND_ID,
				new ImmutablePair<>("Disconnect Request", NoPayloadCommand.class));
		ALL_RECEIVED.put(CONNECT_STATUS_NOTIFICATION_COMMAND_ID,
				new ImmutablePair<>("Connect Status Notification", NoPayloadCommand.class));
	}
}
