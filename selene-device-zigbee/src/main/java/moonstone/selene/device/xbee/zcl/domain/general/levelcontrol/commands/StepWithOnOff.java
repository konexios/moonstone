package moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class StepWithOnOff extends ClusterSpecificCommand<StepWithOnOff> {
	private byte stepMode;
	private byte stepSize;
	private short transitionTime;

	public byte getStepMode() {
		return stepMode;
	}

	public StepWithOnOff withStepMode(byte stepMode) {
		this.stepMode = stepMode;
		return this;
	}

	public byte getStepSize() {
		return stepSize;
	}

	public StepWithOnOff withStepSize(byte stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public short getTransitionTime() {
		return transitionTime;
	}

	public StepWithOnOff withTransitionTime(short transitionTime) {
		this.transitionTime = transitionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LevelControlClusterCommands.STEP_WITH_ON_OFF_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(stepMode);
		buffer.put(stepSize);
		buffer.putShort(transitionTime);
		return buffer.array();
	}

	@Override
	public StepWithOnOff fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		stepMode = buffer.get();
		stepSize = buffer.get();
		transitionTime = buffer.getShort();
		return this;
	}
}
