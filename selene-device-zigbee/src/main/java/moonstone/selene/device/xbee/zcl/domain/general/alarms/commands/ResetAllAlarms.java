package moonstone.selene.device.xbee.zcl.domain.general.alarms.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class ResetAllAlarms extends NoPayloadCommand {
	@Override
	protected int getId() {
		return AlarmsClusterCommands.RESET_ALL_ALARMS_COMMAND_ID;
	}
}
