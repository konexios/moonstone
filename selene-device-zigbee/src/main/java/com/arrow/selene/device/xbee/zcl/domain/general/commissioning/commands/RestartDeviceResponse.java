package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RestartDeviceResponse extends ClusterSpecificCommand<RestartDeviceResponse> {
	private byte[] status = new byte[1];

	public byte[] getStatus() {
		return status;
	}

	public RestartDeviceResponse withStatus(byte status) {
		this.status[0] = status;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.RESTART_DEVICE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return status;
	}

	@Override
	public RestartDeviceResponse fromPayload(byte[] payload) {
		status = payload;
		return this;
	}
}
