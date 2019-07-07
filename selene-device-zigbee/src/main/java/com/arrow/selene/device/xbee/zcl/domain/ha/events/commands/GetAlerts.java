package com.arrow.selene.device.xbee.zcl.domain.ha.events.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class GetAlerts extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceEventsAndAlertClusterCommands.GET_ALERTS_COMMAND_ID;
	}
}
