package moonstone.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class Stop extends NoPayloadCommand {
	@Override
	protected int getId() {
		return LevelControlClusterCommands.STOP_COMMAND_ID;
	}
}
