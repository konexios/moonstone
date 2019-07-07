package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Arm extends ClusterSpecificCommand<Arm> {
	private byte[] armMode = new byte[1];

	public byte[] getArmMode() {
		return armMode;
	}

	public Arm withArmMode(byte armMode) {
		this.armMode[0] = armMode;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.ARM_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return armMode;
	}

	@Override
	public Arm fromPayload(byte[] payload) {
		armMode = payload;
		return this;
	}
}
