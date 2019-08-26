package moonstone.selene.device.xbee.zcl.domain.security.ace.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetZoneInformation extends ClusterSpecificCommand<GetZoneInformation> {
	private byte[] zoneId = new byte[1];

	public byte[] getZoneId() {
		return zoneId;
	}

	public GetZoneInformation withZoneId(byte zoneId) {
		this.zoneId[0] = zoneId;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.GET_ZONE_INFORMATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return zoneId;
	}

	@Override
	public GetZoneInformation fromPayload(byte[] payload) {
		zoneId = payload;
		return this;
	}
}
