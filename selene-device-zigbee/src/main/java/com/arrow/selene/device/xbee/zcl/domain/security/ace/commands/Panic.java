package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class Panic extends NoPayloadCommand {
	@Override
	protected int getId() {
		return SecurityAceClusterCommands.PANIC_COMMAND_ID;
	}
}
