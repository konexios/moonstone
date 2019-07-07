package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.DayMask;
import com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.data.Mode;

public class GetWeeklySchedule extends ClusterSpecificCommand<GetWeeklySchedule> {
	private Set<DayMask> daysOfWeek;
	private Set<Mode> modes;

	public Set<DayMask> getDaysOfWeek() {
		return daysOfWeek;
	}

	public GetWeeklySchedule withDaysOfWeek(Set<DayMask> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
		return this;
	}

	public Set<Mode> getModes() {
		return modes;
	}

	public GetWeeklySchedule withModes(Set<Mode> modes) {
		this.modes = modes;
		return this;
	}

	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.GET_WEEKLY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int days = 0;
		for (DayMask item : daysOfWeek) {
			days |= 1 << item.ordinal();
		}
		buffer.put((byte) days);
		int mode = 0;
		for (Mode item : modes) {
			mode |= 1 << item.ordinal();
		}
		buffer.put((byte) mode);
		return buffer.array();
	}

	@Override
	public GetWeeklySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		daysOfWeek = EnumSet.noneOf(DayMask.class);
		byte days = buffer.get();
		for (DayMask item : DayMask.values()) {
			if ((days >> item.ordinal() & 0x01) == 1) {
				daysOfWeek.add(item);
			}
		}
		modes = EnumSet.noneOf(Mode.class);
		byte mode = buffer.get();
		for (Mode item : Mode.values()) {
			if ((mode >> item.ordinal() & 0x01) == 1) {
				modes.add(item);
			}
		}
		return this;
	}
}
