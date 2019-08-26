package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RssiPing extends ClusterSpecificCommand<RssiPing> {
	private byte[] locationType = new byte[1];

	public byte[] getLocationType() {
		return locationType;
	}

	public RssiPing withLocationType(byte locationType) {
		this.locationType[0] = locationType;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.RSSI_PING_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return locationType;
	}

	@Override
	public RssiPing fromPayload(byte[] payload) {
		locationType = payload;
		return this;
	}
}
