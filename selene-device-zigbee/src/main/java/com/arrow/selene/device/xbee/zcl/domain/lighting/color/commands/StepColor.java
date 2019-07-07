package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class StepColor extends ClusterSpecificCommand<StepColor> {
	private short stepX;
	private short stepY;
	private short transactionTime;

	public short getStepX() {
		return stepX;
	}

	public StepColor withStepX(short stepX) {
		this.stepX = stepX;
		return this;
	}

	public short getStepY() {
		return stepY;
	}

	public StepColor withStepY(short stepY) {
		this.stepY = stepY;
		return this;
	}

	public short getTransactionTime() {
		return transactionTime;
	}

	public StepColor withTransactionTime(short transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.STEP_COLOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(stepX);
		buffer.putShort(stepY);
		buffer.putShort(transactionTime);
		return buffer.array();
	}

	@Override
	public StepColor fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		stepX = buffer.getShort();
		stepY = buffer.getShort();
		transactionTime = buffer.getShort();
		return this;
	}
}
