package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.SnapshotCause;

public class GetSnapshot extends ClusterSpecificCommand<GetSnapshot> {
	private long earliestStartTime;
	private long latestEndTime;
	private int snapshotOffset;
	private Set<SnapshotCause> snapshotCauses;

	public long getEarliestStartTime() {
		return earliestStartTime;
	}

	public GetSnapshot withEarliestStartTime(long earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
		return this;
	}

	public long getLatestEndTime() {
		return latestEndTime;
	}

	public GetSnapshot withLatestEndTime(long latestEndTime) {
		this.latestEndTime = latestEndTime;
		return this;
	}

	public int getSnapshotOffset() {
		return snapshotOffset;
	}

	public GetSnapshot withSnapshotOffset(int snapshotOffset) {
		this.snapshotOffset = snapshotOffset;
		return this;
	}

	public Set<SnapshotCause> getSnapshotCauses() {
		return snapshotCauses;
	}

	public GetSnapshot withSnapshotCauses(Set<SnapshotCause> snapshotCauses) {
		this.snapshotCauses = snapshotCauses;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.GET_SNAPSHOT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) earliestStartTime);
		buffer.putInt((int) latestEndTime);
		buffer.put((byte) snapshotOffset);
		buffer.putInt(SnapshotCause.getByValue(snapshotCauses));
		return buffer.array();
	}

	@Override
	public GetSnapshot fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 13, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		earliestStartTime = Integer.toUnsignedLong(buffer.getInt());
		latestEndTime = Integer.toUnsignedLong(buffer.getInt());
		snapshotOffset = Byte.toUnsignedInt(buffer.get());
		snapshotCauses = SnapshotCause.getByValue(buffer.getInt());
		return this;
	}
}
