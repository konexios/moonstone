package moonstone.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetGroupMembership extends ClusterSpecificCommand<GetGroupMembership> {
	private byte groupCount;
	private List<Short> groups;

	public byte getGroupCount() {
		return groupCount;
	}

	public GetGroupMembership withGroupCount(byte groupCount) {
		this.groupCount = groupCount;
		return this;
	}

	public List<Short> getGroups() {
		return groups;
	}

	public GetGroupMembership withGroups(List<Short> groups) {
		this.groups = groups;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.GET_GROUP_MEMBERSHIP_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(1 + groups.size() * 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(groupCount);
		for (Short group : groups) {
			buffer.putShort(group);
		}
		return buffer.array();
	}

	@Override
	public GetGroupMembership fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		groupCount = buffer.get();
		groups.clear();
		for (int i = 0; i < groupCount; i++) {
			groups.add(buffer.getShort());
		}
		return this;
	}
}
