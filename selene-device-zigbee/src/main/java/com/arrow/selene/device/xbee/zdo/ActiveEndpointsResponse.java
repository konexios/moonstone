package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class ActiveEndpointsResponse {
	private int status;
	private XBee16BitAddress address;
	private byte[] endpoints;

	public ActiveEndpointsResponse(int status, XBee16BitAddress address, byte[] endpoints) {
		this.status = status;
		this.address = address;
		this.endpoints = endpoints;
	}

	public int getStatus() {
		return status;
	}

	public byte[] getEndpoints() {
		return endpoints;
	}

	public XBee16BitAddress getAddress() {
		return address;
	}

	public static ActiveEndpointsResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		int status = Byte.toUnsignedInt(buffer.get());
		XBee16BitAddress address = null;
		byte[] endpoints = null;
		if (status == ZclStatus.SUCCESS) {
			byte[] array = new byte[2];
			buffer.get(array);
			address = new XBee16BitAddress(ByteUtils.swapByteArray(array));
			endpoints = new byte[buffer.get()];
			buffer.get(endpoints);
		}
		return new ActiveEndpointsResponse(status, address, endpoints);
	}
}
