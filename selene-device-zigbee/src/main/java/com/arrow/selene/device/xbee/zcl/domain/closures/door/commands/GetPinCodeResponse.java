package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.UserStatus;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.UserType;

public class GetPinCodeResponse extends ClusterSpecificCommand<GetPinCodeResponse> {
	private int userId;
	private UserStatus userStatus;
	private UserType userType;
	private byte[] code;

	public int getUserId() {
		return userId;
	}

	public GetPinCodeResponse withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public GetPinCodeResponse withUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
		return this;
	}

	public UserType getUserType() {
		return userType;
	}

	public GetPinCodeResponse withUserType(UserType userType) {
		this.userType = userType;
		return this;
	}

	public byte[] getCode() {
		return code;
	}

	public GetPinCodeResponse withCode(byte[] code) {
		this.code = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_PIN_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4 + code.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) userId);
		buffer.put((byte) userStatus.ordinal());
		buffer.put((byte) userType.ordinal());
		buffer.put(code);
		return buffer.array();
	}

	@Override
	protected GetPinCodeResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		userId = Short.toUnsignedInt(buffer.getShort());
		userStatus = UserStatus.values()[Byte.toUnsignedInt(buffer.get())];
		userType = UserType.values()[Byte.toUnsignedInt(buffer.get())];
		code = new byte[buffer.remaining()];
		buffer.get(code);
		return this;
	}
}
