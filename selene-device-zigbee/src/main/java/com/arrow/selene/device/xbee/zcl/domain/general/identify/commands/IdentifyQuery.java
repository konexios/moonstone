package com.arrow.selene.device.xbee.zcl.domain.general.identify.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class IdentifyQuery extends NoPayloadCommand {
	@Override
	protected int getId() {
		return IdentifyClusterCommands.IDENTIFY_QUERY_COMMAND_ID;
	}
}
