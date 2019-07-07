package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Toggle extends ClusterSpecificCommand<Toggle> {
	private byte[] code;

	public byte[] getCode() {
		return code;
	}

	public Toggle withCode(byte[] code) {
		this.code = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.TOGGLE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return code;
	}

	@Override
	protected Toggle fromPayload(byte[] payload) {
		code = payload;
		return this;
	}
}
