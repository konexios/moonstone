package com.arrow.selene.device.xbee.zcl.domain.general.onoff.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class OnWithTimedOff extends ClusterSpecificCommand<OnWithTimedOff> {
	private OnOff onOff;
	private int onTime;
	private int offTime;

	public OnOff getOnOff() {
		return onOff;
	}

	public OnWithTimedOff withOnOff(OnOff onOff) {
		this.onOff = onOff;
		return this;
	}

	public int getOnTime() {
		return onTime;
	}

	public OnWithTimedOff withOnTime(int onTime) {
		this.onTime = onTime;
		return this;
	}

	public int getOffTime() {
		return offTime;
	}

	public OnWithTimedOff withOffTime(int offTime) {
		this.offTime = offTime;
		return this;
	}

	@Override
	protected int getId() {
		return OnOffClusterCommands.ON_WITH_TIMED_OFF_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) onOff.ordinal());
		buffer.putShort((short) onTime);
		buffer.putShort((short) offTime);
		return buffer.array();
	}

	@Override
	public OnWithTimedOff fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		onOff = OnOff.values()[Byte.toUnsignedInt(buffer.get())];
		onTime = Short.toUnsignedInt(buffer.getShort());
		offTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}

	public enum OnOff {
		AT_ALL_TIMES,
		ONLY_WHEN_LIGHT_ON;
	}
}
