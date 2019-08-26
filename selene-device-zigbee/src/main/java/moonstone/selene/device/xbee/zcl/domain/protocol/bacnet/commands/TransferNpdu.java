package moonstone.selene.device.xbee.zcl.domain.protocol.bacnet.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class TransferNpdu extends ClusterSpecificCommand<TransferNpdu> {
	private byte[] npdu;

	public byte[] getNpdu() {
		return npdu;
	}

	public TransferNpdu withNpdu(byte[] npdu) {
		this.npdu = npdu;
		return this;
	}

	@Override
	protected int getId() {
		return BacnetProtocolTunnelClusterCommands.TRANSFER_NPDU_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return npdu;
	}

	@Override
	public TransferNpdu fromPayload(byte[] payload) {
		npdu = payload;
		return this;
	}
}
