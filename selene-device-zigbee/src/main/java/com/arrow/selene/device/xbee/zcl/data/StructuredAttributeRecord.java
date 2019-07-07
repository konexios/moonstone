package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class StructuredAttributeRecord {
    protected static final int ATTRIBUTE_ID_LENGTH = 2;

    private short attributeId;
    private SelectorRecord selector;

    public SelectorRecord getSelector() {
        return selector;
    }

    public void setSelector(SelectorRecord selector) {
        this.selector = selector;
    }

    public short getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(short attributeId) {
        this.attributeId = attributeId;
    }

    public static List<StructuredAttributeRecord> parse(byte... payload) {
        List<StructuredAttributeRecord> records = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        while (buffer.hasRemaining()) {
            StructuredAttributeRecord record = new StructuredAttributeRecord();
            record.setAttributeId(buffer.getShort());
            SelectorRecord selector = new SelectorRecord();
            byte size = buffer.get();
            short[] indices = new short[size];
            for (int i = 0, length = indices.length; i < length; i++) {
                indices[i] = buffer.getShort();
            }
            selector.setIndices(indices);
            records.add(record);
        }
        return records;
    }

    public int calcSize() {
        return ATTRIBUTE_ID_LENGTH + selector.calcSize();
    }

    public byte[] buildPayload() {
        byte[] selectorBytes = selector.buildPayload();
        ByteBuffer buffer = ByteBuffer.allocate(ATTRIBUTE_ID_LENGTH + selectorBytes.length);
        buffer.putShort(getAttributeId());
        buffer.put(selectorBytes);
        return buffer.asReadOnlyBuffer().array();
    }
}
