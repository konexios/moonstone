package moonstone.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WriteAttributeStatusRecord {
    public static final int STATUS_LENGTH = 1;
    protected static final int ATTRIBUTE_ID_LENGTH = 2;

    private byte status;
    private short attributeId;

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public short getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(short attributeId) {
        this.attributeId = attributeId;
    }

    public int calcSize() {
        return STATUS_LENGTH + ATTRIBUTE_ID_LENGTH;
    }

    public byte[] buildPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(calcSize());
        buffer.put(status);
        buffer.putShort(attributeId);
        return buffer.asReadOnlyBuffer().array();
    }

    public static List<WriteAttributeStatusRecord> parse(byte... payload) {
        List<WriteAttributeStatusRecord> records = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        while (buffer.hasRemaining()) {
            WriteAttributeStatusRecord record = new WriteAttributeStatusRecord();
            record.setAttributeId(buffer.getShort());
            record.setStatus(buffer.get());
            records.add(record);
        }
        return records;
    }
}
