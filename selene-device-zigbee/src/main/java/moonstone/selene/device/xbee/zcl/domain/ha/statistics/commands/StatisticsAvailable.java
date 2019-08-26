package moonstone.selene.device.xbee.zcl.domain.ha.statistics.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class StatisticsAvailable extends ClusterSpecificCommand<StatisticsAvailable> {
	private long[] logIds;

	public long[] getLogIds() {
		return logIds;
	}

	public StatisticsAvailable withLogIds(long[] logIds) {
		this.logIds = logIds;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceStatisticsClusterCommands.STATISTICS_AVAILABLE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(1 + logIds.length * 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) logIds.length);
		for (long item : logIds) {
			buffer.putInt((int) item);
		}
		return buffer.array();
	}

	@Override
	public StatisticsAvailable fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 1, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int length = Byte.toUnsignedInt(buffer.get());
		logIds = new long[length];
		for (int i = 0; i < logIds.length; i++) {
			logIds[i] = Integer.toUnsignedLong(buffer.getInt());
		}
		return this;
	}
}
