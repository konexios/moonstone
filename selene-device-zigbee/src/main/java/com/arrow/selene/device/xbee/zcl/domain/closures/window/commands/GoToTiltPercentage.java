package com.arrow.selene.device.xbee.zcl.domain.closures.window.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GoToTiltPercentage extends ClusterSpecificCommand<GoToTiltPercentage> {
	private int tiltPercentage;

	public int getTiltPercentage() {
		return tiltPercentage;
	}

	public GoToTiltPercentage withTiltPercentage(int tiltPercentage) {
		this.tiltPercentage = tiltPercentage;
		return this;
	}

	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_GO_TO_TILT_PERCENTAGE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[]{(byte) tiltPercentage};
	}

	@Override
	public GoToTiltPercentage fromPayload(byte[] payload) {
		tiltPercentage = Byte.toUnsignedInt(payload[0]);
		return this;
	}
}
