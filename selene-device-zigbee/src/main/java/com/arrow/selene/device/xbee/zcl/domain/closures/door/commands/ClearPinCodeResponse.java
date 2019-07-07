package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ClearPinCodeResponse extends ClusterSpecificCommand<ClearPinCodeResponse> {
	private byte[] status = new byte[1];

	public ClearPinCodeResponse withStatus(byte status) {
		this.status[0] = status;
		return this;
	}

	public byte getStatus() {
		return status[0];
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.CLEAR_PIN_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return status;
	}

	@Override
	protected ClearPinCodeResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		status = payload;
		return this;
	}
}
