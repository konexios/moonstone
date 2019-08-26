package moonstone.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.AttributeStatusRecord;

public class ConfigureReportingResponse extends GeneralResponse<ConfigureReportingResponse> {
	private List<AttributeStatusRecord> statuses = Collections.emptyList();

	public List<AttributeStatusRecord> getStatuses() {
		return statuses;
	}

	@Override
	protected ConfigureReportingResponse fromPayload(byte[] payload) {
		if (payload.length != 1) {
			statuses = AttributeStatusRecord.parse(payload);
		}
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.CONFIGURE_REPORTING_RSP;
	}
}
