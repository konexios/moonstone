package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.lighting.color.data.StepMode;

public class StepHue extends ClusterSpecificCommand<StepHue> {
	private StepMode stepMode;
	private int stepSize;
	private int transactionTime;

	public StepMode getStepMode() {
		return stepMode;
	}

	public StepHue withStepMode(StepMode stepMode) {
		this.stepMode = stepMode;
		return this;
	}

	public int getStepSize() {
		return stepSize;
	}

	public StepHue withStepSize(int stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public StepHue withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.STEP_HUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put((byte) stepMode.ordinal());
		buffer.put((byte) stepSize);
		buffer.put((byte) transactionTime);
		return buffer.array();
	}

	@Override
	public StepHue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		stepMode = StepMode.values()[Byte.toUnsignedInt(buffer.get())];
		stepSize = Byte.toUnsignedInt(buffer.get());
		transactionTime = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
