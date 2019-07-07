package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class StructuredWriteAttributeStatusRecord {
    private SelectorRecord selector;
    private WriteAttributeStatusRecord status;

    public SelectorRecord getSelector() {
        return selector;
    }

    public void setSelector(SelectorRecord selector) {
        this.selector = selector;
    }

    public byte getStatus() {
        return status.getStatus();
    }

    public void setStatus(byte status) {
        this.status.setStatus(status);
    }

    public short getAttributeId() {
        return status.getAttributeId();
    }

    public void setAttributeId(short attributeId) {
        status.setAttributeId(attributeId);
    }

    public int calcSize() {
        return WriteAttributeStatusRecord.STATUS_LENGTH + WriteAttributeStatusRecord.ATTRIBUTE_ID_LENGTH + selector.calcSize();
    }

    public byte[] buildPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(calcSize());
        buffer.put(status.getStatus());
        buffer.putShort(status.getAttributeId());
        buffer.put(selector.buildPayload());
        return buffer.asReadOnlyBuffer().array();
    }

    public static List<StructuredWriteAttributeStatusRecord> parse(byte... payload) {
        List<StructuredWriteAttributeStatusRecord> records = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        while (buffer.hasRemaining()) {
            StructuredWriteAttributeStatusRecord record = new StructuredWriteAttributeStatusRecord();
            record.status = new WriteAttributeStatusRecord();
            record.setStatus(buffer.get());
            record.setAttributeId(buffer.getShort());
            record.setSelector(SelectorRecord.internalParse(buffer));
            records.add(record);
        }
        return records;
    }
}
