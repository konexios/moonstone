package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.ZclDataType;

public class AttributeExtendedRecord {
	private int attributeId;
	private ZclDataType attributeDataType;
	private int accessType;

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public ZclDataType getAttributeDataType() {
		return attributeDataType;
	}

	public void setAttributeDataType(ZclDataType attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

	public static List<AttributeExtendedRecord> parse(byte... payload) {
		List<AttributeExtendedRecord> records = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		while (buffer.hasRemaining()) {
			AttributeExtendedRecord record = new AttributeExtendedRecord();
			record.setAttributeId(buffer.getShort());
			ZclDataType zclDataType = ZclDataType.findById(buffer.get());
			record.setAttributeDataType(zclDataType);
			record.setAccessType(buffer.get());
			records.add(record);
		}
		return records;
	}
}
