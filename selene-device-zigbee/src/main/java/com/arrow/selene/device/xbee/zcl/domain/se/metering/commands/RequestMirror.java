package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class RequestMirror extends NoPayloadCommand {
	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.REQUEST_MIRROR_COMMAND_ID;
	}
}
