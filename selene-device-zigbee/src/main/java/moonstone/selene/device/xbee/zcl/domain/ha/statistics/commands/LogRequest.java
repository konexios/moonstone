package moonstone.selene.device.xbee.zcl.domain.ha.statistics.commands;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LogRequest extends ClusterSpecificCommand<LogRequest> {
	private long logId;

	public long getLogId() {
		return logId;
	}

	public LogRequest withLogId(long logId) {
		this.logId = logId;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceStatisticsClusterCommands.LOG_REQUEST_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.intToByteArray((int) logId));
	}

	@Override
	public LogRequest fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		logId = ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(payload));
		return this;
	}
}
