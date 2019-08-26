package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.xbee.zcl.data.AttributeStatusRecord;

public class ReadReportingConfiguration extends GeneralRequest {
	private List<AttributeStatusRecord> statuses = Collections.emptyList();

	public ReadReportingConfiguration(int manufacturerCode, List<AttributeStatusRecord> statuses) {
		super(manufacturerCode);
		this.statuses = statuses;
	}

	@Override
	protected byte[] toPayload() {
		int size = 0;
		for (AttributeStatusRecord status : statuses) {
			size += status.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (AttributeStatusRecord status : statuses) {
			buffer.put(status.buildPayload());
		}
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.READ_REPORTING_CONFIGURATION;
	}
}
