package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class ClearWeeklySchedule extends NoPayloadCommand {
	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.CLEAR_WEEKLY_SCHEDULE_COMMAND_ID;
	}
}
