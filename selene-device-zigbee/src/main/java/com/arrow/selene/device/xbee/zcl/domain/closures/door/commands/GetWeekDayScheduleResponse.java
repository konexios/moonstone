package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.DayMask;

public class GetWeekDayScheduleResponse extends ClusterSpecificCommand<GetWeekDayScheduleResponse> {
	private int scheduleId;
	private int userId;
	private int status;
	private Set<DayMask> dayMasks;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

	public int getScheduleId() {
		return scheduleId;
	}

	public GetWeekDayScheduleResponse withScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public GetWeekDayScheduleResponse withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public GetWeekDayScheduleResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public Set<DayMask> getDayMasks() {
		return dayMasks;
	}

	public GetWeekDayScheduleResponse withDayMasks(Set<DayMask> dayMasks) {
		this.dayMasks = dayMasks;
		return this;
	}

	public int getStartHour() {
		return startHour;
	}

	public GetWeekDayScheduleResponse withStartHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public GetWeekDayScheduleResponse withStartMinute(int startMinute) {
		this.startMinute = startMinute;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public GetWeekDayScheduleResponse withEndHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public GetWeekDayScheduleResponse withEndMinute(int endMinute) {
		this.endMinute = endMinute;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_WEEKDAY_SCHEDULE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 9 : 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) scheduleId);
		buffer.putShort((short) userId);
		buffer.put((byte) status);
		int dayMask = 0;
		for (DayMask item : dayMasks) {
			dayMask |= 1 << item.ordinal();
		}
		buffer.put((byte) dayMask);
		buffer.put((byte) startHour);
		buffer.put((byte) startMinute);
		buffer.put((byte) endHour);
		buffer.put((byte) endMinute);
		return buffer.array();
	}

	@Override
	protected GetWeekDayScheduleResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4 || payload.length == 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		scheduleId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		status = Byte.toUnsignedInt(buffer.get());
		if (status == ZclStatus.SUCCESS) {
			byte dayMask = buffer.get();
			dayMasks = EnumSet.noneOf(DayMask.class);
			for (DayMask item : DayMask.values()) {
				if ((dayMask >> item.ordinal() & 0x01) == 1) {
					dayMasks.add(item);
				}
			}
			startHour = Byte.toUnsignedInt(buffer.get());
			startMinute = Byte.toUnsignedInt(buffer.get());
			endHour = Byte.toUnsignedInt(buffer.get());
			endMinute = Byte.toUnsignedInt(buffer.get());
		} else {
			dayMasks = EnumSet.noneOf(DayMask.class);
			startHour = 0;
			startMinute = 0;
			endHour = 0;
			endMinute = 0;
		}
		return this;
	}
}
