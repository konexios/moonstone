package com.arrow.selene.device.xbee.zcl.domain.general.groups.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class ViewGroupResponse extends ClusterSpecificCommand<ViewGroupResponse> {
	private int status;
	private short groupId;
	private String groupName;

	public int getStatus() {
		return status;
	}

	public ViewGroupResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public short getGroupId() {
		return groupId;
	}

	public ViewGroupResponse withGroupId(short groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public ViewGroupResponse withGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

	@Override
	protected int getId() {
		return GroupsClusterCommands.VIEW_GROUP_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3 + status == ZclStatus.SUCCESS ? groupName.length() : 1);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.putShort(groupId);
		if (status == ZclStatus.SUCCESS) {
			buffer.put(groupName.getBytes(StandardCharsets.UTF_8));
		} else {
			buffer.put((byte) 0x00);
		}
		return buffer.array();
	}

	@Override
	public ViewGroupResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = buffer.get();
		groupId = buffer.getShort();
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		groupName = status == ZclStatus.SUCCESS ? new String(data, Charset.defaultCharset()) : "";
		return this;
	}
}
