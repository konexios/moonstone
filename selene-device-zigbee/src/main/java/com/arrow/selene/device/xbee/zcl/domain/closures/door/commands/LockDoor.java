package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LockDoor extends ClusterSpecificCommand<LockDoor> {
	private byte[] code;

	public byte[] getCode() {
		return code;
	}

	public LockDoor withCode(byte code) {
		this.code[0] = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.LOCK_DOOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return code;
	}

	@Override
	protected LockDoor fromPayload(byte[] payload) {
		code = payload;
		return this;
	}
}
