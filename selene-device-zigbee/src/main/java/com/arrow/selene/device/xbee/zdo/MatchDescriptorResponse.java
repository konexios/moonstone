package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class MatchDescriptorResponse {
	public static byte[] toPayload(int sequence, int status, XBee16BitAddress address, int... endpoints) {
		ByteBuffer buffer = ByteBuffer.allocate(5 + endpoints.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) sequence);
		buffer.put((byte) status);
		buffer.put(ByteUtils.swapByteArray(address.getValue()));
		buffer.put((byte) endpoints.length);
		for (int endpoint : endpoints) {
			buffer.put((byte) endpoint);
		}
		return buffer.array();
	}
}
