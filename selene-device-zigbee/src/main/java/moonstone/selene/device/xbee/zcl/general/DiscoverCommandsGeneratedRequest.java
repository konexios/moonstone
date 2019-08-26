package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DiscoverCommandsGeneratedRequest extends GeneralRequest {
	int startCommandId;
	int maxCommandIds;

	public DiscoverCommandsGeneratedRequest(int manufacturerCode, boolean fromServer, int startCommandId, int
			maxCommandIds) {
		super(manufacturerCode);
		header.setFromServer(fromServer);
		this.startCommandId = startCommandId;
		this.maxCommandIds = maxCommandIds;
	}

	@Override
	protected byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) startCommandId);
		buffer.put((byte) maxCommandIds);
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DISCOVER_COMMANDS_GENERATED;
	}
}
