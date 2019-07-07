package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RequestOwnLocation extends ClusterSpecificCommand<RequestOwnLocation> {
	private byte[] requestingAddress;

	public byte[] getRequestingAddress() {
		return requestingAddress;
	}

	public RequestOwnLocation withRequestingAddress(byte[] requestingAddress) {
		this.requestingAddress = requestingAddress;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.REQUEST_OWN_LOCATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return requestingAddress;
	}

	@Override
	public RequestOwnLocation fromPayload(byte[] payload) {
		requestingAddress = payload;
		return this;
	}
}
