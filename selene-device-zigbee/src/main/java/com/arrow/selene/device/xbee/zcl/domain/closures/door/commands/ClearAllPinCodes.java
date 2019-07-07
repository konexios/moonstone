package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class ClearAllPinCodes extends NoPayloadCommand {
	@Override
	protected int getId() {
		return DoorLockClusterCommands.CLEAR_ALL_PINS_COMMAND_ID;
	}
}
