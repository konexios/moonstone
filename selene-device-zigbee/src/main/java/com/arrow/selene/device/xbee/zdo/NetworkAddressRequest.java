package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class NetworkAddressRequest {
	public static byte[] toPayload(byte sequence, XBee64BitAddress address) {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(sequence);
		buffer.put(ByteUtils.swapByteArray(address.getValue()));
		buffer.put((byte) 0x00);
		buffer.put((byte) 0x00);
		return buffer.array();
	}
}
