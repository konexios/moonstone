package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BindResponse {
	private int status;

	public BindResponse(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static BindResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		int status = Byte.toUnsignedInt(buffer.get());
		return new BindResponse(status);
	}
}
