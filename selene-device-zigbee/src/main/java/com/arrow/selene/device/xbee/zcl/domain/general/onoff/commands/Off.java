package com.arrow.selene.device.xbee.zcl.domain.general.onoff.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class Off extends NoPayloadCommand {
	@Override
	protected int getId() {
		return OnOffClusterCommands.OFF_COMMAND_ID;
	}
}
