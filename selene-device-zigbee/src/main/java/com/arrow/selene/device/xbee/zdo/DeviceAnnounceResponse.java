package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class DeviceAnnounceResponse {
	public static byte[] toPayload(int sequence, XBee16BitAddress address16, XBee64BitAddress address64, int caps) {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) sequence);
		buffer.put(ByteUtils.swapByteArray(address16.getValue()));
		buffer.put(ByteUtils.swapByteArray(address64.getValue()));
		buffer.put((byte) caps);
		return buffer.array();
	}
}
