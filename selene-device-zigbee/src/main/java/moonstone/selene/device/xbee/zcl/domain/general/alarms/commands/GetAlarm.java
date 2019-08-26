package moonstone.selene.device.xbee.zcl.domain.general.alarms.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class GetAlarm extends NoPayloadCommand {
	@Override
	protected int getId() {
		return AlarmsClusterCommands.GET_ALARM_COMMAND_ID;
	}
}
