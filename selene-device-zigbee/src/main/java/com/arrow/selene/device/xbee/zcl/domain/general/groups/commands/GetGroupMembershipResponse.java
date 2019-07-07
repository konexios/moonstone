package com.arrow.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetGroupMembershipResponse extends ClusterSpecificCommand<GetGroupMembershipResponse> {
	private byte capacity;
	private byte groupCount;
	private List<Short> groups;

	public byte getCapacity() {
		return capacity;
	}

	public GetGroupMembershipResponse withCapacity(byte capacity) {
		this.capacity = capacity;
		return this;
	}

	public byte getGroupCount() {
		return groupCount;
	}

	public GetGroupMembershipResponse withGroupCount(byte groupCount) {
		this.groupCount = groupCount;
		return this;
	}

	public List<Short> getGroups() {
		return groups;
	}

	public GetGroupMembershipResponse withGroups(List<Short> groups) {
		this.groups = groups;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.GET_GROUP_MEMBERSHIP_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2 + groups.size() * 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(capacity);
		buffer.put(groupCount);
		for (Short group : groups) {
			buffer.putShort(group);
		}
		return buffer.array();
	}

	@Override
	public GetGroupMembershipResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		capacity = buffer.get();
		groupCount = buffer.get();
		groups.clear();
		for (int i = 0; i < groupCount; i++) {
			groups.add(buffer.getShort());
		}
		return this;
	}
}
