package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.SnapshotScheduleConfirmation;

public class ScheduleSnapshotResponse extends ClusterSpecificCommand<ScheduleSnapshotResponse> {
	private long issuerEventId;
	private int snapshotScheduleId;
	private SnapshotScheduleConfirmation snapshotScheduleConfirmation;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ScheduleSnapshotResponse withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public int getSnapshotScheduleId() {
		return snapshotScheduleId;
	}

	public ScheduleSnapshotResponse withSnapshotScheduleId(int snapshotScheduleId) {
		this.snapshotScheduleId = snapshotScheduleId;
		return this;
	}

	public SnapshotScheduleConfirmation getSnapshotScheduleConfirmation() {
		return snapshotScheduleConfirmation;
	}

	public ScheduleSnapshotResponse withSnapshotScheduleConfirmation(
			SnapshotScheduleConfirmation snapshotScheduleConfirmation) {
		this.snapshotScheduleConfirmation = snapshotScheduleConfirmation;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.SCHEDULE_SNAPSHOT_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) snapshotScheduleId);
		buffer.put((byte) snapshotScheduleConfirmation.ordinal());
		return buffer.array();
	}

	@Override
	public ScheduleSnapshotResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		snapshotScheduleId = Byte.toUnsignedInt(buffer.get());
		snapshotScheduleConfirmation = SnapshotScheduleConfirmation.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
