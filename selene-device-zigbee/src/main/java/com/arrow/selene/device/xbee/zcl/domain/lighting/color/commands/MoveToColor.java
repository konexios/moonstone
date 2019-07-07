package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveToColor extends ClusterSpecificCommand<MoveToColor> {
	private int colorX;
	private int colorY;
	private int transactionTime;

	public int getColorX() {
		return colorX;
	}

	public MoveToColor withColorX(int colorX) {
		this.colorX = colorX;
		return this;
	}

	public int getColorY() {
		return colorY;
	}

	public MoveToColor withColorY(int colorY) {
		this.colorY = colorY;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public MoveToColor withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_TO_COLOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) colorX);
		buffer.putShort((short) colorY);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public MoveToColor fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		colorX = Short.toUnsignedInt(buffer.getShort());
		colorY = Short.toUnsignedInt(buffer.getShort());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
