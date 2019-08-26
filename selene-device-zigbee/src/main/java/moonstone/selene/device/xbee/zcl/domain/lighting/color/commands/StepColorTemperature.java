package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.MoveMode;

public class StepColorTemperature extends ClusterSpecificCommand<StepColorTemperature> {
	private MoveMode moveMode;
	private int stepSize;
	private int transitionTime;
	private int temperatureMin;
	private int temperatureMax;

	public MoveMode getMoveMode() {
		return moveMode;
	}

	public StepColorTemperature withMoveMode(MoveMode moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public int getStepSize() {
		return stepSize;
	}

	public StepColorTemperature withStepSize(int stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	public int getTransitionTime() {
		return transitionTime;
	}

	public StepColorTemperature withTransitionTime(int transitionTime) {
		this.transitionTime = transitionTime;
		return this;
	}

	public int getTemperatureMin() {
		return temperatureMin;
	}

	public StepColorTemperature withTemperatureMin(int temperatureMin) {
		this.temperatureMin = temperatureMin;
		return this;
	}

	public int getTemperatureMax() {
		return temperatureMax;
	}

	public StepColorTemperature withTemperatureMax(int temperatureMax) {
		this.temperatureMax = temperatureMax;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.STEP_COLOR_TEMPERATURE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.put((byte) moveMode.ordinal());
		buffer.putShort((short) stepSize);
		buffer.putShort((short) transitionTime);
		buffer.putShort((short) temperatureMin);
		buffer.putShort((short) temperatureMax);
		return buffer.array();
	}

	@Override
	public StepColorTemperature fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = MoveMode.values()[Byte.toUnsignedInt(buffer.get())];
		stepSize = Short.toUnsignedInt(buffer.getShort());
		transitionTime = Short.toUnsignedInt(buffer.getShort());
		temperatureMin = Short.toUnsignedInt(buffer.getShort());
		temperatureMax = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
