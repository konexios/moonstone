package com.arrow.selene.device.xbee.zcl.domain.general.appliance.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class OverloadPause extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.OVERLOAD_PAUSE_COMMAND_ID;
	}
}
