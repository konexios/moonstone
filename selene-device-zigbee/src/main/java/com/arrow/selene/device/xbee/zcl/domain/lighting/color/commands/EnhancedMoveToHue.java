package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.lighting.color.data.Direction;

public class EnhancedMoveToHue extends ClusterSpecificCommand<EnhancedMoveToHue> {
	private int hue;
	private Direction direction;
	private int transactionTime;

	public int getHue() {
		return hue;
	}

	public EnhancedMoveToHue withHue(int hue) {
		this.hue = hue;
		return this;
	}

	public Direction getDirection() {
		return direction;
	}

	public EnhancedMoveToHue withDirection(Direction direction) {
		this.direction = direction;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public EnhancedMoveToHue withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.ENHANCED_MOVE_TO_HUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) hue);
		buffer.put((byte) direction.ordinal());
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public EnhancedMoveToHue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		hue = Short.toUnsignedInt(buffer.getShort());
		direction = Direction.values()[Byte.toUnsignedInt(buffer.get())];
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
