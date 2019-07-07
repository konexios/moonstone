package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.data.SetpointMode;

public class SetpointRaiseLower extends ClusterSpecificCommand<SetpointRaiseLower> {
	private SetpointMode mode;
	private byte amount;

	public SetpointMode getMode() {
		return mode;
	}

	public SetpointRaiseLower withMode(SetpointMode mode) {
		this.mode = mode;
		return this;
	}

	public byte getAmount() {
		return amount;
	}

	public SetpointRaiseLower withAmount(byte amount) {
		this.amount = amount;
		return this;
	}

	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.SETPOINT_RAISE_LOWER_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) mode.ordinal());
		buffer.put(amount);
		return buffer.array();
	}

	@Override
	public SetpointRaiseLower fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		mode = SetpointMode.values()[Byte.toUnsignedInt(buffer.get())];
		amount = buffer.get();
		return this;
	}
}
