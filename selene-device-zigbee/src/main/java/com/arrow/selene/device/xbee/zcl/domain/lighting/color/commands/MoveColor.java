package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveColor extends ClusterSpecificCommand<MoveColor> {
	private int rateX;
	private int rateY;

	public int getRateX() {
		return rateX;
	}

	public MoveColor withRateX(int rateX) {
		this.rateX = rateX;
		return this;
	}

	public int getRateY() {
		return rateY;
	}

	public MoveColor withRateY(int rateY) {
		this.rateY = rateY;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_COLOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) rateX);
		buffer.putShort((short) rateY);
		return buffer.array();
	}

	@Override
	public MoveColor fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		rateX = Short.toUnsignedInt(buffer.getShort());
		rateY = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
