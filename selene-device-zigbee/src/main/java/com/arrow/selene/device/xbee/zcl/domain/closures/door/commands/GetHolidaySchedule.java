package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetHolidaySchedule extends ClusterSpecificCommand<GetHolidaySchedule> {
	private byte[] holidayScheduleId = new byte[1];

	public byte[] getHolidayScheduleId() {
		return holidayScheduleId;
	}

	public GetHolidaySchedule withHolidayScheduleId(byte holidayScheduleId) {
		this.holidayScheduleId[0] = holidayScheduleId;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_HOLIDAY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return holidayScheduleId;
	}

	@Override
	protected GetHolidaySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		holidayScheduleId = payload;
		return this;
	}
}
