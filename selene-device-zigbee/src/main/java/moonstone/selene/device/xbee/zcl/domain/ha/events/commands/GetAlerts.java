package moonstone.selene.device.xbee.zcl.domain.ha.events.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class GetAlerts extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceEventsAndAlertClusterCommands.GET_ALERTS_COMMAND_ID;
	}
}
