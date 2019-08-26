package moonstone.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RemoveAllScenesResponse extends ClusterSpecificCommand<RemoveAllScenesResponse> {
	private int status;
	private int groupId;

	public int getStatus() {
		return status;
	}

	public RemoveAllScenesResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getGroupId() {
		return groupId;
	}

	public RemoveAllScenesResponse withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.REMOVE_ALL_SCENES_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.putShort((short) groupId);
		return buffer.array();
	}

	@Override
	public RemoveAllScenesResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = Byte.toUnsignedInt(buffer.get());
		groupId = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
