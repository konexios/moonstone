package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class UnlockWithTimeout extends ClusterSpecificCommand<UnlockWithTimeout> {
	private int timeout;
	private byte[] code;

	public int getTimeout() {
		return timeout;
	}

	public UnlockWithTimeout withTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public byte[] getCode() {
		return code;
	}

	public UnlockWithTimeout withCode(byte[] code) {
		this.code = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.UNLOCK_WITH_TIMEOUT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2 + code.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) timeout);
		buffer.put(code);
		return buffer.array();
	}

	@Override
	protected UnlockWithTimeout fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		timeout = Short.toUnsignedInt(buffer.getShort());
		code = new byte[buffer.remaining()];
		buffer.get(code);
		return this;
	}
}
