package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.StructuredWriteAttributeRecord;

public class WriteAttributesStructured extends GeneralRequest {
	List<StructuredWriteAttributeRecord> attributes = Collections.emptyList();

	public WriteAttributesStructured(int manufacturerCode, List<StructuredWriteAttributeRecord> attributes) {
		super(manufacturerCode);
		this.attributes = attributes;
	}

	@Override
	protected byte[] toPayload() {
		int size = 0;
		for (StructuredWriteAttributeRecord attribute : attributes) {
			size += attribute.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (StructuredWriteAttributeRecord attribute : attributes) {
			buffer.put(attribute.buildPayload());
		}
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.WRITE_ATTRIBUTES_STRUCTURED;
	}
}
