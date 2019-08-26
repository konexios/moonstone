package moonstone.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class AddGroup extends ClusterSpecificCommand<AddGroup> {
	private short groupId;
	private String groupName;

	public short getGroupId() {
		return groupId;
	}

	public AddGroup withGroupId(short groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public AddGroup withGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.ADD_GROUP_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2 + groupName.length());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(groupId);
		buffer.put(groupName.getBytes(StandardCharsets.UTF_8));
		return buffer.array();
	}

	@Override
	public AddGroup fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		groupId = buffer.getShort();
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		groupName = new String(data, StandardCharsets.UTF_8);
		return this;
	}
}
