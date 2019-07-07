package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class GetYearDayScheduleResponse extends ClusterSpecificCommand<GetYearDayScheduleResponse> {
	private int scheduleId;
	private int userId;
	private int status;
	private long startTime;
	private long endTime;

	public int getScheduleId() {
		return scheduleId;
	}

	public GetYearDayScheduleResponse withScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public GetYearDayScheduleResponse withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public GetYearDayScheduleResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public GetYearDayScheduleResponse withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public GetYearDayScheduleResponse withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_YEARDAY_SCHEDULE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 12 : 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) scheduleId);
		buffer.putShort((short) userId);
		buffer.put((byte) status);
		buffer.putInt((int) startTime);
		buffer.putInt((int) endTime);
		return buffer.array();
	}

	@Override
	protected GetYearDayScheduleResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4 || payload.length == 12, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		scheduleId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		status = Byte.toUnsignedInt(buffer.get());
		if(status == ZclStatus.SUCCESS) {
			startTime = Integer.toUnsignedLong(buffer.getInt());
			endTime = Integer.toUnsignedLong(buffer.getInt());
		} else {
			startTime = 0;
			endTime = 0;
		}
		return this;
	}
}
