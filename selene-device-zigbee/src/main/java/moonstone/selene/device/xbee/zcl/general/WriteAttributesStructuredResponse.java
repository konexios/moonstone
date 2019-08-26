package moonstone.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.StructuredWriteAttributeStatusRecord;

@SuppressWarnings("rawtypes")
public class WriteAttributesStructuredResponse extends GeneralResponse {
	private List<StructuredWriteAttributeStatusRecord> statuses = Collections.emptyList();

	public List<StructuredWriteAttributeStatusRecord> getStatuses() {
		return statuses;
	}

	@Override
	protected WriteAttributesStructuredResponse fromPayload(byte[] payload) {
		statuses = StructuredWriteAttributeStatusRecord.parse(payload);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.WRITE_ATTRIBUTES_STRUCTURED_RSP;
	}
}
