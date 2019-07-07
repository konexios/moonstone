package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.lighting.color.data.StepMode;

public class EnhancedStepHue extends ClusterSpecificCommand<EnhancedStepHue> {
	private StepMode stepMode;
	private int stepSize;
	private int transactionTime;

	public StepMode getStepMode() {
		return stepMode;
	}

	public EnhancedStepHue withStepMode(StepMode stepMode) {
		this.stepMode = stepMode;
		return this;
	}

	public int getStepSize() {
		return stepSize;
	}

	public EnhancedStepHue withStepSize(int stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public EnhancedStepHue withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.ENHANCED_STEP_HUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put((byte) stepMode.ordinal());
		buffer.putShort((short) stepSize);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public EnhancedStepHue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		stepMode = StepMode.values()[Byte.toUnsignedInt(buffer.get())];
		stepSize = Short.toUnsignedInt(buffer.getShort());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
