package moonstone.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.ReadAttributeStatusRecord;

public class ReadAttributeResponse extends GeneralResponse<ReadAttributeResponse> {
	private List<ReadAttributeStatusRecord> records = Collections.emptyList();

	public List<ReadAttributeStatusRecord> getRecords() {
		return records;
	}

	@Override
	protected ReadAttributeResponse fromPayload(byte[] payload) {
		records = ReadAttributeStatusRecord.parse(payload);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.READ_ATTRIBUTES_RSP;
	}
}
