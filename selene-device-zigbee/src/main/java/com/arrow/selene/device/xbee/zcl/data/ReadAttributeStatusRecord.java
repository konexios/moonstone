package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.RawValue;
import com.arrow.selene.device.xbee.zcl.ZclDataType;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class ReadAttributeStatusRecord {
	private short attributeId;
	private byte status;
	private ZclDataType attributeDataType;
	private byte[] rawValue;
	private boolean valid;

	public short getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public ZclDataType getAttributeDataType() {
		return attributeDataType;
	}

	public void setAttributeDataType(ZclDataType attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public void setRawValue(byte[] rawValue) {
		this.rawValue = rawValue;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public static List<ReadAttributeStatusRecord> parse(byte... payload) {
		List<ReadAttributeStatusRecord> records = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		while (buffer.hasRemaining()) {
			ReadAttributeStatusRecord record = new ReadAttributeStatusRecord();
			record.setAttributeId(buffer.getShort());
			byte status = buffer.get();
			record.setStatus(status);
			if (status == ZclStatus.SUCCESS) {
				ZclDataType zclDataType = ZclDataType.findById(buffer.get());
				record.setAttributeDataType(zclDataType);
				switch (zclDataType) {
					case DATA_ARRAY:
					case DATA_STRUCTURE:
					case DATA_SET:
					case DATA_BAG:
						// TODO parsing after debug on real hardware
						break;
					default:
						RawValue rawValue = zclDataType.getRawValue(buffer);
						record.setRawValue(rawValue.getValue());
						record.setValid(rawValue.isValid());
						break;
				}
			}
			records.add(record);
		}
		return records;
	}

	public String getStringValue() {
		return attributeDataType.getStringValue(rawValue);
	}
}
