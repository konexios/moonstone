package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ArmResponse extends ClusterSpecificCommand<ArmResponse> {
	private byte[] armNotification = new byte[1];

	public byte[] getArmNotification() {
		return armNotification;
	}

	public ArmResponse withArmNotification(byte armNotification) {
		this.armNotification[0] = armNotification;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.ARM_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return armNotification;
	}

	@Override
	public ArmResponse fromPayload(byte[] payload) {
		armNotification = payload;
		return this;
	}
}
