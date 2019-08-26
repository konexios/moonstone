package moonstone.selene.device.xbee.zcl.domain.general.partition.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class PartitionClusterCommands {
	public static final int MULTIPLE_ACK_COMMAND_ID = 0x00;
	public static final int READ_HANDSHAKE_PARAM_RESPONSE_COMMAND_ID = 0x01;

	public static final int TRANSFER_PARTITIONED_FRAME_COMMAND_ID = 0x00;
	public static final int READ_HANDSHAKE_PARAM_COMMAND_ID = 0x01;
	public static final int WRITE_HANDSHAKE_PARAM_COMMAND_ID = 0x02;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(MULTIPLE_ACK_COMMAND_ID, "Multiple Ack");
		ALL_GENERATED.put(READ_HANDSHAKE_PARAM_RESPONSE_COMMAND_ID, "Read Handshake Parameter Response");

		ALL_RECEIVED.put(TRANSFER_PARTITIONED_FRAME_COMMAND_ID,
				new ImmutablePair<>("Transfer Partitioned Frame", NoPayloadCommand.class));
		ALL_RECEIVED.put(READ_HANDSHAKE_PARAM_COMMAND_ID,
				new ImmutablePair<>("Read Handshake Parameter", NoPayloadCommand.class));
		ALL_RECEIVED.put(WRITE_HANDSHAKE_PARAM_COMMAND_ID,
				new ImmutablePair<>("Write Handshake Parameter", NoPayloadCommand.class));
	}
}
