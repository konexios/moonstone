package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.StructuredAttributeRecord;

public class ReadAttributesStructured extends GeneralRequest {
	private List<StructuredAttributeRecord> attributes = Collections.emptyList();

	public ReadAttributesStructured(int manufacturerCode, List<StructuredAttributeRecord> attributes) {
		super(manufacturerCode);
		this.attributes = attributes;
	}

	@Override
	protected byte[] toPayload() {
		int size = 0;
		for (StructuredAttributeRecord attribute : attributes) {
			size += attribute.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (StructuredAttributeRecord attribute : attributes) {
			buffer.put(attribute.buildPayload());
		}
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.READ_ATTRIBUTES_STRUCTURED;
	}
}
