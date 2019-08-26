package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.MoveMode;

public class MoveSaturation extends ClusterSpecificCommand<MoveSaturation> {
	private MoveMode moveMode;
	private int rate;

	public MoveMode getMoveMode() {
		return moveMode;
	}

	public MoveSaturation withMoveMode(MoveMode moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public int getRate() {
		return rate;
	}

	public MoveSaturation withRate(int rate) {
		this.rate = rate;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_SATURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.put((byte) moveMode.ordinal());
		buffer.put((byte) rate);
		return buffer.array();
	}

	@Override
	public MoveSaturation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = MoveMode.values()[Byte.toUnsignedInt(buffer.get())];
		rate = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
