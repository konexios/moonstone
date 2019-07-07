package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class GetRelayStatusLog extends NoPayloadCommand {
	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.GET_RELAY_STATUS_LOG_COMMAND_ID;
	}
}
