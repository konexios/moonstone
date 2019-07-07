package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.arrow.acs.Loggable;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.RawValue;
import com.arrow.selene.device.xbee.zcl.ZclDataType;

public class AttributeRecord {
	static Loggable LOG = new Loggable(AttributeRecord.class.getName()) {
	};
	protected static final int ATTRIBUTE_ID_LENGTH = 2;
	protected static final int DATA_TYPE_LENGTH = 1;

	private int attributeId;
	private ZclDataType attributeDataType;
	private byte[] rawValue;

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

	public byte[] getRawValue() {
		return rawValue;
	}

	public void setRawValue(byte[] rawValue) {
		this.rawValue = rawValue;
	}

	public static List<AttributeRecord> parse(byte... payload) {
		String method = "parse";
		List<AttributeRecord> records = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		while (buffer.hasRemaining()) {
			AttributeRecord record = new AttributeRecord();
			record.setAttributeId(Short.toUnsignedInt(buffer.getShort()));
			ZclDataType zclDataType = ZclDataType.findById(Byte.toUnsignedInt(buffer.get()));
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
				LOG.logInfo(method, "zclDataType: %s, rawValue: %s", zclDataType.name(),
				        Hex.encodeHexString(rawValue.getValue()));
				break;
			}
			records.add(record);
		}
		return records;
	}

	public int calcSize() {
		return ATTRIBUTE_ID_LENGTH + DATA_TYPE_LENGTH + rawValue.length;
	}

	public byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(calcSize());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) attributeId);
		buffer.put((byte) attributeDataType.getId());
		buffer.put(rawValue);
		return buffer.array();
	}

	public String getValue() {
		return attributeDataType.getStringValue(rawValue);
	}

	public SensorData<?> toData(String name) {
		return attributeDataType.toData(name, rawValue);
	}
}
