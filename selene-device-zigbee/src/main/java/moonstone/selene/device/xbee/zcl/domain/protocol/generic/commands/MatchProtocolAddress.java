package moonstone.selene.device.xbee.zcl.domain.protocol.generic.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MatchProtocolAddress extends ClusterSpecificCommand<MatchProtocolAddress> {
	private byte[] protocolAddress;

	public MatchProtocolAddress(byte[] protocolAddress) {
		this.protocolAddress = protocolAddress;
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
		return protocolAddress;
	}

	@Override
	public MatchProtocolAddress fromPayload(byte[] payload) {
		protocolAddress = payload;
		return this;
	}
}
