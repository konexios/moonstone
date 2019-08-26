package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.MoveMode;

public class MoveColorTemperature extends ClusterSpecificCommand<MoveColorTemperature> {
	private MoveMode moveMode;
	private int rate;
	private int temperatureMin;
	private int temperatureMax;

	public MoveMode getMoveMode() {
		return moveMode;
	}

	public MoveColorTemperature withMoveMode(MoveMode moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public int getRate() {
		return rate;
	}

	public MoveColorTemperature withRate(int rate) {
		this.rate = rate;
		return this;
	}

	public int getTemperatureMin() {
		return temperatureMin;
	}

	public MoveColorTemperature withTemperatureMin(int temperatureMin) {
		this.temperatureMin = temperatureMin;
		return this;
	}

	public int getTemperatureMax() {
		return temperatureMax;
	}

	public MoveColorTemperature withTemperatureMax(int temperatureMax) {
		this.temperatureMax = temperatureMax;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_COLOR_TEMPERATURE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.put((byte) moveMode.ordinal());
		buffer.putShort((short) rate);
		buffer.putShort((short) temperatureMin);
		buffer.putShort((short) temperatureMax);
		return buffer.array();
	}

	@Override
	public MoveColorTemperature fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = MoveMode.values()[Byte.toUnsignedInt(buffer.get())];
		rate = Short.toUnsignedInt(buffer.getShort());
		temperatureMin = Short.toUnsignedInt(buffer.getShort());
		temperatureMax = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
