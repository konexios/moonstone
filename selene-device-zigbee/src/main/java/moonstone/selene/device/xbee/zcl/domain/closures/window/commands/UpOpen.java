package moonstone.selene.device.xbee.zcl.domain.closures.window.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class UpOpen extends NoPayloadCommand {
	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_UP_OPEN_COMMAND_ID;
	}
}
