package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.DayMask;
import com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.data.Mode;
import com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.data.Transition;

public class GetWeeklyScheduleResponse extends ClusterSpecificCommand<GetWeeklyScheduleResponse> {
	private Set<DayMask> daysOfWeek;
	private Set<Mode> modes;
	private Transition[] transitions;

	public Set<DayMask> getDaysOfWeek() {
		return daysOfWeek;
	}

	public GetWeeklyScheduleResponse withDaysOfWeek(Set<DayMask> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
		return this;
	}

	public Set<Mode> getModes() {
		return modes;
	}

	public GetWeeklyScheduleResponse withModes(Set<Mode> modes) {
		this.modes = modes;
		return this;
	}

	public Transition[] getTransitions() {
		return transitions;
	}

	public GetWeeklyScheduleResponse withTransitions(Transition[] transitions) {
		this.transitions = transitions;
		return this;
	}

	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.SET_WEEKLY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		boolean heat = modes.contains(Mode.HEAT_SETPOINT_PRESENT);
		boolean cool = modes.contains(Mode.COOL_SETPOINT_PRESENT);
		int length = transitions.length * 2;
		ByteBuffer buffer = ByteBuffer.allocate(3 + length + (heat ? length : 0) + (cool ? length : 0));
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) transitions.length);
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
		for (Transition item : transitions) {
			buffer.putShort((short) item.getTransitionTime());
			if (heat) {
				buffer.putShort(item.getHeatSetpoint());
			}
			if (cool) {
				buffer.putShort(item.getCoolSetpoint());
			}
		}
		return buffer.array();
	}

	@Override
	public GetWeeklyScheduleResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		transitions = new Transition[buffer.get()];
		daysOfWeek = EnumSet.noneOf(DayMask.class);
		int days = buffer.get();
		for (DayMask item : DayMask.values()) {
			if ((days >> item.ordinal() & 0x01) == 1) {
				daysOfWeek.add(item);
			}
		}
		modes = EnumSet.noneOf(Mode.class);
		int mode = buffer.get();
		for (Mode item : Mode.values()) {
			if ((mode >> item.ordinal() & 0x01) == 1) {
				modes.add(item);
			}
		}
		boolean heat = modes.contains(Mode.HEAT_SETPOINT_PRESENT);
		boolean cool = modes.contains(Mode.COOL_SETPOINT_PRESENT);
		for (int i = 0; i < transitions.length; i++) {
			int transitionTime = Short.toUnsignedInt(buffer.getShort());
			short heatSetpoint = 0;
			if (heat) {
				heatSetpoint = buffer.getShort();
			}
			short coolSetpoint = 0;
			if (cool) {
				coolSetpoint = buffer.getShort();
			}
			transitions[i] = new Transition().withTransitionTime(transitionTime).withHeatSetpoint(heatSetpoint)
					.withCoolSetpoint(coolSetpoint);
		}
		return this;
	}
}
