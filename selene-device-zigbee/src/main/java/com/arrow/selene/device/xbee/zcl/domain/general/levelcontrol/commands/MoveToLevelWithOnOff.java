package com.arrow.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveToLevelWithOnOff extends ClusterSpecificCommand<MoveToLevelWithOnOff> {
	private byte level;
	private short transitionTime;

	public byte getLevel() {
		return level;
	}

	public MoveToLevelWithOnOff withLevel(byte level) {
		this.level = level;
		return this;
	}

	public short getTransitionTime() {
		return transitionTime;
	}

	public MoveToLevelWithOnOff withTransitionTime(short transitionTime) {
		this.transitionTime = transitionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LevelControlClusterCommands.MOVE_TO_LEVEL_WITH_ON_OFF_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(level);
		buffer.putShort(transitionTime);
		return buffer.array();
	}

	@Override
	public MoveToLevelWithOnOff fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		level = buffer.get();
		transitionTime = buffer.getShort();
		return this;
	}
}
