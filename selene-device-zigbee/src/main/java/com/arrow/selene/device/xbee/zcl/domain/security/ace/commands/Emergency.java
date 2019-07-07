package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class Emergency extends NoPayloadCommand {
	@Override
	protected int getId() {
		return SecurityAceClusterCommands.EMERGENCY_COMMAND_ID;
	}
}
