package com.arrow.selene.device.xbee.zcl.domain.general.groups.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class RemoveAllGroups extends NoPayloadCommand {
	@Override
	protected int getId() {
		return GroupsClusterCommands.REMOVE_ALL_GROUPS_COMMAND_ID;
	}
}
