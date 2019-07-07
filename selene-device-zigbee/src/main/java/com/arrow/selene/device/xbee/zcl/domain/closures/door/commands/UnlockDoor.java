package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class UnlockDoor extends ClusterSpecificCommand<UnlockDoor> {
	private byte[] code;

	public byte[] getCode() {
		return code;
	}

	public UnlockDoor withCode(byte[] code) {
		this.code = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.UNLOCK_DOOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return code;
	}

	@Override
	protected UnlockDoor fromPayload(byte[] payload) {
		code = payload;
		return this;
	}
}
