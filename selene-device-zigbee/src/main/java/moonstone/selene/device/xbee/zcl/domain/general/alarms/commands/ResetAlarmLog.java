package moonstone.selene.device.xbee.zcl.domain.general.alarms.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class ResetAlarmLog extends NoPayloadCommand {
	@Override
	protected int getId() {
		return AlarmsClusterCommands.RESET_ALARM_LOG_COMMAND_ID;
	}
}
