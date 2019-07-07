package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class SimpleDescriptorRequest {
	public static byte[] toPayload(byte sequence, XBee16BitAddress address, byte endpoint) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(sequence);
		buffer.put(ByteUtils.swapByteArray(address.getValue()));
		buffer.put(endpoint);
		return buffer.array();
	}
}
