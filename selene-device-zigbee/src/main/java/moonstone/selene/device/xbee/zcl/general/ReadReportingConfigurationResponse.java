package moonstone.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.AttributeReportingConfigRecord;

@SuppressWarnings("rawtypes")
public class ReadReportingConfigurationResponse extends GeneralResponse {
	private List<AttributeReportingConfigRecord> configs = Collections.emptyList();

	public List<AttributeReportingConfigRecord> getConfigs() {
		return configs;
	}

	@Override
	protected ReadReportingConfigurationResponse fromPayload(byte[] payload) {
		configs = AttributeReportingConfigRecord.parse(payload);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.READ_REPORTING_CONFIGURATION_RSP;
	}
}
