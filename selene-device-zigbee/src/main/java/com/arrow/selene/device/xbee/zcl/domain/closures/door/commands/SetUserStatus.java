package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.UserStatus;

public class SetUserStatus extends ClusterSpecificCommand<SetUserStatus> {
	private int userId;
	private UserStatus userStatus;

	public int getUserId() {
		return userId;
	}

	public SetUserStatus withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public SetUserStatus withUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.SET_USER_STATUS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) userId);
		buffer.put((byte) userStatus.ordinal());
		return buffer.array();
	}

	@Override
	protected SetUserStatus fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length ==3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		userId = Short.toUnsignedInt(buffer.getShort());
		userStatus = UserStatus.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
