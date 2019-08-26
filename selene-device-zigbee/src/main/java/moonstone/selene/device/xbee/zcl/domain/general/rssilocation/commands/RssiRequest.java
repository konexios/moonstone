package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class RssiRequest extends NoPayloadCommand {
	@Override
	protected int getId() {
		return RssiLocationClusterCommands.RSSI_REQUEST_COMMAND_ID;
	}
}
