package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class Fire extends NoPayloadCommand {
	@Override
	protected int getId() {
		return SecurityAceClusterCommands.FIRE_COMMAND_ID;
	}
}
