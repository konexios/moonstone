package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SetYearDaySchedule extends ClusterSpecificCommand<SetYearDaySchedule> {
	private int scheduleId;
	private int userId;
	private long startTime;
	private long endTime;

	public int getScheduleId() {
		return scheduleId;
	}

	public SetYearDaySchedule withScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public SetYearDaySchedule withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public SetYearDaySchedule withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public SetYearDaySchedule withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.SET_YEARDAY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) scheduleId);
		buffer.putShort((short) userId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) endTime);
		return buffer.array();
	}

	@Override
	protected SetYearDaySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		scheduleId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		endTime = Integer.toUnsignedLong(buffer.getInt());
		return this;
	}
}
