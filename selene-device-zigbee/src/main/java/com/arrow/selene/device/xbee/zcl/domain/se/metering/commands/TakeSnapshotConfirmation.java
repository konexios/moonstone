package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.SnapshotConfirmation;

public class TakeSnapshotConfirmation extends ClusterSpecificCommand<TakeSnapshotConfirmation> {
	private long snapshotId;
	private SnapshotConfirmation snapshotConfirmation;

	public long getSnapshotId() {
		return snapshotId;
	}

	public TakeSnapshotConfirmation withSnapshotId(long snapshotId) {
		this.snapshotId = snapshotId;
		return this;
	}

	public SnapshotConfirmation getSnapshotConfirmation() {
		return snapshotConfirmation;
	}

	public TakeSnapshotConfirmation withSnapshotConfirmation(SnapshotConfirmation snapshotConfirmation) {
		this.snapshotConfirmation = snapshotConfirmation;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.TAKE_SNAPSHOT_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) snapshotId);
		buffer.put((byte) snapshotConfirmation.ordinal());
		return buffer.array();
	}

	@Override
	public TakeSnapshotConfirmation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		snapshotId = Integer.toUnsignedLong(buffer.getInt());
		snapshotConfirmation = SnapshotConfirmation.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
