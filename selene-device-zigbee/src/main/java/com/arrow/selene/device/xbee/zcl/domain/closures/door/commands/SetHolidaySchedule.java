package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.attributes.OperatingMode;

public class SetHolidaySchedule extends ClusterSpecificCommand<SetHolidaySchedule> {
	private int holidayScheduleId;
	private long startTime;
	private long endTime;
	private OperatingMode operatingMode;

	public int getHolidayScheduleId() {
		return holidayScheduleId;
	}

	public SetHolidaySchedule withHolidayScheduleId(int holidayScheduleId) {
		this.holidayScheduleId = holidayScheduleId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public SetHolidaySchedule withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public SetHolidaySchedule withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	public OperatingMode getOperatingMode() {
		return operatingMode;
	}

	public SetHolidaySchedule withOperatingMode(OperatingMode operatingMode) {
		this.operatingMode = operatingMode;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.SET_HOLIDAY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) holidayScheduleId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) endTime);
		buffer.put((byte) operatingMode.ordinal());
		return buffer.array();
	}

	@Override
	protected SetHolidaySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 10, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		holidayScheduleId = Byte.toUnsignedInt(buffer.get());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		endTime = Integer.toUnsignedLong(buffer.getInt());
		operatingMode = OperatingMode.getByValue(buffer.get());
		return this;
	}
}
