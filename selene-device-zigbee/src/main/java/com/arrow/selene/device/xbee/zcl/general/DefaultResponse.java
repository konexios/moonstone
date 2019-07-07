package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;

public class DefaultResponse extends GeneralResponse<DefaultResponse> {
	private int commandId;
	private int statusCode;

	public int getCommandId() {
		return commandId;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	protected DefaultResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		commandId = Byte.toUnsignedInt(buffer.get());
		statusCode = Byte.toUnsignedInt(buffer.get());
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DEFAULT_RSP;
	}
}
