package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class GetZoneIdMap extends NoPayloadCommand {
	@Override
	protected int getId() {
		return SecurityAceClusterCommands.GET_ZONE_ID_MAP_COMMAND_ID;
	}
}
