package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MirrorRemoved extends ClusterSpecificCommand<MirrorRemoved> {
	private int removedEndpointId;

	public int getRemovedEndpointId() {
		return removedEndpointId;
	}

	public MirrorRemoved withRemovedEndpointId(int removedEndpointId) {
		this.removedEndpointId = removedEndpointId;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.MIRROR_REMOVED_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) removedEndpointId);
		return buffer.array();
	}

	@Override
	public MirrorRemoved fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		removedEndpointId = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
