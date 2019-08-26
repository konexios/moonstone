package moonstone.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ViewGroup extends ClusterSpecificCommand<ViewGroup> {
	private short groupId;

	public short getGroupId() {
		return groupId;
	}

	public ViewGroup withGroupId(short groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.VIEW_GROUP_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(groupId).array();
	}

	@Override
	public ViewGroup fromPayload(byte[] payload) {
		groupId = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).getShort();
		return this;
	}
}
