package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;

public class SelectorRecord {
    public static final int INDICATOR_LENGTH = 1;
    public static final int INDEX_LENGTH = 2;

    private short[] indices;

    public short[] getIndices() {
        return indices;
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }

    static SelectorRecord internalParse(ByteBuffer buffer) {
        SelectorRecord selector = new SelectorRecord();
        short[] indices = new short[buffer.get()];
        for (int i = 0, length = indices.length; i < length; i++) {
            indices[i] = buffer.getShort();
        }
        selector.setIndices(indices);
        return selector;
    }

    public int calcSize() {
        return INDICATOR_LENGTH + indices.length * INDEX_LENGTH;
    }

    public byte[] buildPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(calcSize());
        buffer.put(Integer.valueOf(indices.length).byteValue());
        for (short index : indices) {
            buffer.putShort(index);
        }
        return buffer.asReadOnlyBuffer().array();
    }
}
