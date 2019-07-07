package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.AttributeExtendedRecord;

public class DiscoverAttributesExtendedResponse extends GeneralResponse<DiscoverAttributesExtendedResponse> {
	private boolean discoveryComplete;
	private List<AttributeExtendedRecord> attributes;

	public boolean isDiscoveryComplete() {
		return discoveryComplete;
	}

	public List<AttributeExtendedRecord> getAttributes() {
		return attributes;
	}

	@Override
	protected DiscoverAttributesExtendedResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		discoveryComplete = buffer.get() == 0x01;
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		attributes = AttributeExtendedRecord.parse(data);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DISCOVER_ATTRIBUTES_EXTENDED_RSP;
	}
}
