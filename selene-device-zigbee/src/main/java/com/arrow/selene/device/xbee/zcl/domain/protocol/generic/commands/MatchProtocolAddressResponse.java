package com.arrow.selene.device.xbee.zcl.domain.protocol.generic.commands;

import java.nio.ByteBuffer;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MatchProtocolAddressResponse extends ClusterSpecificCommand<MatchProtocolAddressResponse> {
	private byte[] deviceIeeeAddress;
	private byte[] protocolAddress;

	public MatchProtocolAddressResponse(byte[] deviceIeeeAddress, byte[] protocolAddress) {
		this.deviceIeeeAddress = deviceIeeeAddress;
		this.protocolAddress = protocolAddress;
	}

	public byte[] getDeviceIeeeAddress() {
		return deviceIeeeAddress;
	}

	public byte[] getProtocolAddress() {
		return protocolAddress;
	}

	@Override
	protected int getId() {
		return GenericTunnelClusterCommands.MATCH_PROTOCOL_ADDRESS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(8 + protocolAddress.length);
		buffer.put(deviceIeeeAddress);
		buffer.put(protocolAddress);
		return buffer.array();
	}

	@Override
	public MatchProtocolAddressResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		deviceIeeeAddress = new byte[8];
		buffer.get(deviceIeeeAddress);
		protocolAddress = new byte[buffer.remaining()];
		buffer.get(protocolAddress);
		return this;
	}
}
