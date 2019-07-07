package com.arrow.selene.device.xbee.zcl.domain.general.onoff.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class Toggle extends NoPayloadCommand {
	@Override
	protected int getId() {
		return OnOffClusterCommands.TOGGLE_COMMAND_ID;
	}
}
