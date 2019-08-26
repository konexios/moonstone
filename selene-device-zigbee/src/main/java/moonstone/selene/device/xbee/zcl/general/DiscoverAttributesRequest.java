package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DiscoverAttributesRequest extends GeneralRequest {
	int startAttributeId;
	int maxAttributeIds;

	public DiscoverAttributesRequest(int manufacturerCode, int startAttributeId, int maxAttributeIds) {
		super(manufacturerCode);
		this.startAttributeId = startAttributeId;
		this.maxAttributeIds = maxAttributeIds;
	}

	@Override
	protected byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) startAttributeId);
		buffer.put((byte) maxAttributeIds);
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DISCOVER_ATTRIBUTES;
	}
}
