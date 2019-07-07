package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;

@SuppressWarnings("rawtypes")
public class DiscoverAttributesResponse extends GeneralResponse {
	private boolean discoveryComplete;
	private List<AttributeRecord> attributes;

	public boolean isDiscoveryComplete() {
		return discoveryComplete;
	}

	public List<AttributeRecord> getAttributes() {
		return attributes;
	}

	@Override
	protected DiscoverAttributesResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		discoveryComplete = buffer.get() == 0x01;
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		attributes = AttributeRecord.parse(data);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DISCOVER_ATTRIBUTES_RSP;
	}
}
