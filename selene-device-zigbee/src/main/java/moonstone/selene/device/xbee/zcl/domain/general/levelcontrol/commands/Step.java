package moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Step extends ClusterSpecificCommand<Step> {
	private byte stepMode;
	private byte stepSize;
	private short transitionTime;

	public byte getStepMode() {
		return stepMode;
	}

	public Step withStepMode(byte stepMode) {
		this.stepMode = stepMode;
		return this;
	}

	public byte getStepSize() {
		return stepSize;
	}

	public Step withStepSize(byte stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public short getTransitionTime() {
		return transitionTime;
	}

	public Step withTransitionTime(short transitionTime) {
		this.transitionTime = transitionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LevelControlClusterCommands.STEP_COMMAND_ID;
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
	public Step fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		stepMode = buffer.get();
		stepSize = buffer.get();
		transitionTime = buffer.getShort();
		return this;
	}
}
