package com.arrow.selene.device.xbee.zcl.domain.general.pollcontrol.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class CheckIn extends NoPayloadCommand {
	@Override
	protected int getId() {
		return PollControlClusterCommands.CHECK_IN_COMMAND_ID;
	}
}
