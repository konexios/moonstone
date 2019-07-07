package com.arrow.selene.device.xbee.zcl.domain.ha.statistics.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LogNotification extends ClusterSpecificCommand<LogNotification> {
	private long timestamp;
	private long logId;
	private byte[] data;

	public long getTimestamp() {
		return timestamp;
	}

	public LogNotification withTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public long getLogId() {
		return logId;
	}

	public LogNotification withLogId(long logId) {
		this.logId = logId;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public LogNotification withData(byte[] data) {
		this.data = data;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceStatisticsClusterCommands.LOG_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(12 + data.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) timestamp);
		buffer.putInt((int) logId);
		buffer.putInt(data.length);
		if (data.length > 0) {
			buffer.put(data);
		}
		return buffer.array();
	}

	@Override
	public LogNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 12, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		timestamp = Integer.toUnsignedLong(buffer.getInt());
		logId = Integer.toUnsignedLong(buffer.getInt());
		long length = Integer.toUnsignedLong(buffer.getInt());
		if (length > 0 && length == buffer.remaining()) {
			data = new byte[buffer.remaining()];
			buffer.get(data);
		}
		return this;
	}
}
