package moonstone.selene.device.xbee.zcl.domain.general.onoff.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class On extends NoPayloadCommand {
	@Override
	protected int getId() {
		return OnOffClusterCommands.ON_COMMAND_ID;
	}
}
