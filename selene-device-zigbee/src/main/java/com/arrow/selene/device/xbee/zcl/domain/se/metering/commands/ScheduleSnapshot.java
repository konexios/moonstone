package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.SnapshotSchedule;

public class ScheduleSnapshot extends ClusterSpecificCommand<ScheduleSnapshot> {
	private long issuerEventId;
	private int commandIndex;
	private SnapshotSchedule[] snapshotSchedules;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ScheduleSnapshot withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public ScheduleSnapshot withCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
		return this;
	}

	public SnapshotSchedule[] getSnapshotSchedules() {
		return snapshotSchedules;
	}

	public ScheduleSnapshot withSnapshotSchedules(SnapshotSchedule[] snapshotSchedules) {
		this.snapshotSchedules = snapshotSchedules;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.PUBLISH_SNAPSHOT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6 + snapshotSchedules.length * 13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) commandIndex);
		buffer.put((byte) snapshotSchedules.length);
		for (SnapshotSchedule item : snapshotSchedules) {
			buffer.put(item.toPayload());
		}
		return buffer.array();
	}

	@Override
	public ScheduleSnapshot fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		commandIndex = Byte.toUnsignedInt(buffer.get());
		snapshotSchedules = new SnapshotSchedule[Byte.toUnsignedInt(buffer.get())];
		for (int i = 0; i < snapshotSchedules.length; i++) {
			byte[] bytes = new byte[13];
			buffer.get(bytes);
			snapshotSchedules[i] = SnapshotSchedule.fromPayload(bytes);
		}
		return this;
	}
}
