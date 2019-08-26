package moonstone.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import moonstone.selene.device.xbee.zcl.ZclDataType;

public class StructuredWriteAttributeRecord {
	private SelectorRecord selector;
	private AttributeRecord attribute;

	public SelectorRecord getSelector() {
		return selector;
	}

	public void setSelector(SelectorRecord selector) {
		this.selector = selector;
	}

	public int getAttributeId() {
		return attribute.getAttributeId();
	}

	public void setAttributeId(int attributeId) {
		attribute.setAttributeId(attributeId);
	}

	public ZclDataType getAttributeDataType() {
		return attribute.getAttributeDataType();
	}

	public void setAttributeDataType(ZclDataType attributeDataType) {
		attribute.setAttributeDataType(attributeDataType);
	}

	public byte[] getRawValue() {
		return attribute.getRawValue();
	}

	public void setRawValue(byte[] value) {
		attribute.setRawValue(value);
	}

	public static List<StructuredWriteAttributeRecord> parse(byte... payload) {
		List<StructuredWriteAttributeRecord> records = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		while (buffer.hasRemaining()) {
			StructuredWriteAttributeRecord record = new StructuredWriteAttributeRecord();
			AttributeRecord attribute = new AttributeRecord();
			attribute.setAttributeId(buffer.getShort());
			record.setSelector(SelectorRecord.internalParse(buffer));
			attribute.setAttributeDataType(ZclDataType.findById(buffer.get()));
			// FIXME parse value
			records.add(record);
		}
		return records;
	}

	public int calcSize() {
		return AttributeRecord.ATTRIBUTE_ID_LENGTH + selector.calcSize() + AttributeRecord.DATA_TYPE_LENGTH +
				attribute.calcSize();
	}

	public byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(calcSize());
		buffer.putShort((short) getAttributeId());
		buffer.put(selector.buildPayload());
		buffer.put((byte) getAttributeDataType().getId());
		buffer.put(getRawValue());
		return buffer.asReadOnlyBuffer().array();
	}
}
