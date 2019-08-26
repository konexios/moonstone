package moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Move extends ClusterSpecificCommand<Move> {
	private byte moveMode;
	private byte rate;

	public byte getMoveMode() {
		return moveMode;
	}

	public Move withMoveMode(byte moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public byte getRate() {
		return rate;
	}

	public Move withRate(byte rate) {
		this.rate = rate;
		return this;
	}

	@Override
	protected int getId() {
		return LevelControlClusterCommands.MOVE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put(moveMode);
		buffer.put(rate);
		return buffer.array();
	}

	@Override
	public Move fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = buffer.get();
		rate = buffer.get();
		return this;
	}
}
