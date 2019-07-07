package com.arrow.selene.device.xbee.zcl.domain.se.metering.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import com.digi.xbee.api.utils.ByteUtils;

public class SnapshotSchedule {
	private int snapshotScheduleId;
	private long snapshotStartTime;
	private int frequency;
	private FrequencyType frequencyType;
	private FrequencyWildcard frequencyWildcard;
	private SnapshotPayloadType snapshotPayloadType;
	private Set<SnapshotCause> snapshotCauses;

	public int getSnapshotScheduleId() {
		return snapshotScheduleId;
	}

	public SnapshotSchedule withSnapshotScheduleId(int snapshotScheduleId) {
		this.snapshotScheduleId = snapshotScheduleId;
		return this;
	}

	public long getSnapshotStartTime() {
		return snapshotStartTime;
	}

	public SnapshotSchedule withSnapshotStartTime(long snapshotStartTime) {
		this.snapshotStartTime = snapshotStartTime;
		return this;
	}

	public int getFrequency() {
		return frequency;
	}

	public SnapshotSchedule withFrequency(int frequency) {
		this.frequency = frequency;
		return this;
	}

	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	public SnapshotSchedule withFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
		return this;
	}

	public FrequencyWildcard getFrequencyWildcard() {
		return frequencyWildcard;
	}

	public SnapshotSchedule withFrequencyWildcard(FrequencyWildcard frequencyWildcard) {
		this.frequencyWildcard = frequencyWildcard;
		return this;
	}

	public SnapshotPayloadType getSnapshotPayloadType() {
		return snapshotPayloadType;
	}

	public SnapshotSchedule withSnapshotPayloadType(SnapshotPayloadType snapshotPayloadType) {
		this.snapshotPayloadType = snapshotPayloadType;
		return this;
	}

	public Set<SnapshotCause> getSnapshotCauses() {
		return snapshotCauses;
	}

	public SnapshotSchedule withSnapshotCauses(Set<SnapshotCause> snapshotCauses) {
		this.snapshotCauses = snapshotCauses;
		return this;
	}

	public static SnapshotSchedule fromPayload(byte... bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int snapshotScheduleId = Byte.toUnsignedInt(buffer.get());
		long snapshotStartTime = Integer.toUnsignedLong(buffer.getInt());
		byte[] schedule = new byte[3];
		buffer.get(schedule);
		int value = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(schedule));
		int frequency = value & 0x0fffff;
		FrequencyType frequencyType = FrequencyType.values()[value >> 20 & 0x03];
		FrequencyWildcard frequencyWildcard = FrequencyWildcard.values()[value >> 22 & 0x03];
		SnapshotPayloadType snapshotPayloadType = SnapshotPayloadType.values()[Byte.toUnsignedInt(buffer.get())];
		Set<SnapshotCause> snapshotCauses = SnapshotCause.getByValue(buffer.getInt());
		return new SnapshotSchedule().withSnapshotScheduleId(snapshotScheduleId).withSnapshotStartTime(
				snapshotStartTime).withFrequency(frequency).withFrequencyType(frequencyType).withFrequencyWildcard(
				frequencyWildcard).withSnapshotPayloadType(snapshotPayloadType).withSnapshotCauses(snapshotCauses);
	}

	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) snapshotScheduleId);
		buffer.putInt((int) snapshotStartTime);
		int value = frequency;
		value |= frequencyType.ordinal() << 20;
		value |= frequencyWildcard.ordinal() << 22;
		byte[] bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(value));
		buffer.put(bytes, 0, 3);
		buffer.put((byte) snapshotPayloadType.ordinal());
		buffer.putInt(SnapshotCause.getByValue(snapshotCauses));
		return buffer.array();
	}
}
