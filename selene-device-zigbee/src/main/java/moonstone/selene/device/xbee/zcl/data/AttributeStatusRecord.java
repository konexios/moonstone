package moonstone.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AttributeStatusRecord {
    private static final int DIRECTION_LENGTH = 1;
    private static final int ATTRIBUTE_ID_LENGTH = 2;

    private int status;
    private int direction;
    private int attributeId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public static List<AttributeStatusRecord> parse(byte... payload) {
        List<AttributeStatusRecord> records = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        while (buffer.hasRemaining()) {
            AttributeStatusRecord record = new AttributeStatusRecord();
            record.setStatus(Byte.toUnsignedInt(buffer.get()));
            record.setDirection(Byte.toUnsignedInt(buffer.get()));
            record.setAttributeId(Short.toUnsignedInt(buffer.getShort()));
            records.add(record);
        }
        return records;
    }

    public int calcSize() {
        return DIRECTION_LENGTH + ATTRIBUTE_ID_LENGTH;
    }

    public byte[] buildPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(calcSize());
        buffer.put((byte) direction);
        buffer.putShort((short) attributeId);
        return buffer.asReadOnlyBuffer().array();
    }
}
