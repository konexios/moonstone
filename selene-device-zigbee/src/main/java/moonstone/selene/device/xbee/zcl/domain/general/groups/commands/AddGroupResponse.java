package moonstone.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class AddGroupResponse extends ClusterSpecificCommand<AddGroupResponse> {
	private byte status;
	private short groupId;

	public byte getStatus() {
		return status;
	}

	public AddGroupResponse withStatus(byte status) {
		this.status = status;
		return this;
	}

	public short getGroupId() {
		return groupId;
	}

	public AddGroupResponse withGroupId(short groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.ADD_GROUP_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(status);
		buffer.putShort(groupId);
		return buffer.array();
	}

	@Override
	public AddGroupResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = buffer.get();
		groupId = buffer.getShort();
		return this;
	}
}
