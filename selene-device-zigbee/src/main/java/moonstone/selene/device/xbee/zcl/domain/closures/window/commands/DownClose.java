package moonstone.selene.device.xbee.zcl.domain.closures.window.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class DownClose extends NoPayloadCommand {
	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_DOWN_CLOSE_COMMAND_ID;
	}
}
