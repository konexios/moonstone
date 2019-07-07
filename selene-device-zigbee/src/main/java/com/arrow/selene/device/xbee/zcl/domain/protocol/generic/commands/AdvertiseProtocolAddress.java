package com.arrow.selene.device.xbee.zcl.domain.protocol.generic.commands;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class AdvertiseProtocolAddress extends ClusterSpecificCommand<AdvertiseProtocolAddress> {
	private byte[] protocolAddress;

	public byte[] getProtocolAddress() {
		return protocolAddress;
	}

	public AdvertiseProtocolAddress withProtocolAddress(byte[] protocolAddress) {
		this.protocolAddress = protocolAddress;
		return this;
	}

	@Override
	protected int getId() {
		return GenericTunnelClusterCommands.ADVERTISE_PROTOCOL_ADDRESS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return protocolAddress;
	}

	@Override
	public AdvertiseProtocolAddress fromPayload(byte[] payload) {
		protocolAddress = payload;
		return this;
	}
}
