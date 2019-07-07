package com.arrow.selene.device.xbee.zcl.domain.closures.window.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class GoToLiftValue extends ClusterSpecificCommand<GoToLiftValue> {
	private int liftValue;

	public int getLiftValue() {
		return liftValue;
	}

	public GoToLiftValue withLiftValue(int liftValue) {
		this.liftValue = liftValue;
		return this;
	}

	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_GO_TO_LIFT_VALUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) liftValue));
	}

	@Override
	public GoToLiftValue fromPayload(byte[] payload) {
		liftValue = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}
