package com.arrow.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.nio.ByteBuffer;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveWithOnOff extends ClusterSpecificCommand<MoveWithOnOff> {
	private byte moveMode;
	private byte rate;

	public byte getMoveMode() {
		return moveMode;
	}

	public MoveWithOnOff withMoveMode(byte moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public byte getRate() {
		return rate;
	}

	public MoveWithOnOff withRate(byte rate) {
		this.rate = rate;
		return this;
	}

	@Override
	protected int getId() {
		return LevelControlClusterCommands.MOVE_WITH_ON_OFF_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put(moveMode);
		buffer.put(rate);
		return buffer.array();
	}

	@Override
	public MoveWithOnOff fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = buffer.get();
		rate = buffer.get();
		return this;
	}
}
