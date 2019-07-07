package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SaveStartupParametersResponse extends ClusterSpecificCommand<SaveStartupParametersResponse> {
	private byte[] status = new byte[1];

	public byte[] getStatus() {
		return status;
	}

	public SaveStartupParametersResponse withStatus(byte status) {
		this.status[0] = status;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.SAVE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return status;
	}

	@Override
	public SaveStartupParametersResponse fromPayload(byte[] payload) {
		status = payload;
		return this;
	}
}
