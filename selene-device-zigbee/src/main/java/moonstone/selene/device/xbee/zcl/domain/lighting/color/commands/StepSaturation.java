package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.StepMode;

public class StepSaturation extends ClusterSpecificCommand<StepSaturation> {
	private StepMode stepMode;
	private int stepSize;
	private int transactionTime;

	public StepMode getStepMode() {
		return stepMode;
	}

	public StepSaturation withStepMode(StepMode stepMode) {
		this.stepMode = stepMode;
		return this;
	}

	public int getStepSize() {
		return stepSize;
	}

	public StepSaturation withStepSize(int stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public StepSaturation withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.STEP_SATURATION_COMMAND_ID;
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
	public StepSaturation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		stepMode = StepMode.values()[Byte.toUnsignedInt(buffer.get())];
		stepSize = Byte.toUnsignedInt(buffer.get());
		transactionTime = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
