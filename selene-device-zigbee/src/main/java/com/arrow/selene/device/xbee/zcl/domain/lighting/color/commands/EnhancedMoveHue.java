package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.lighting.color.data.MoveMode;

public class EnhancedMoveHue extends ClusterSpecificCommand<EnhancedMoveHue> {
	private MoveMode moveMode;
	private int rate;

	public MoveMode getMoveMode() {
		return moveMode;
	}

	public EnhancedMoveHue withMoveMode(MoveMode moveMode) {
		this.moveMode = moveMode;
		return this;
	}

	public int getRate() {
		return rate;
	}

	public EnhancedMoveHue withRate(int rate) {
		this.rate = rate;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.ENHANCED_MOVE_HUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put((byte) moveMode.ordinal());
		buffer.putShort((short) rate);
		return buffer.array();
	}

	@Override
	public EnhancedMoveHue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		moveMode = MoveMode.values()[Byte.toUnsignedInt(buffer.get())];
		rate = Short.toUnsignedInt(buffer.getShort());
		return this;
	}

}
