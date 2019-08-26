package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.AttributeRecord;

public class WriteAttributes extends GeneralRequest {
	private List<AttributeRecord> attributes = Collections.emptyList();

	public WriteAttributes(int manufacturerCode, List<AttributeRecord> attributes) {
		super(manufacturerCode);
		this.attributes = attributes;
	}

	@Override
	protected byte[] toPayload() {
		int size = 0;
		for (AttributeRecord attribute : attributes) {
			size += attribute.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (AttributeRecord attribute : attributes) {
			buffer.put(attribute.buildPayload());
		}
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.WRITE_ATTRIBUTES;
	}
}
