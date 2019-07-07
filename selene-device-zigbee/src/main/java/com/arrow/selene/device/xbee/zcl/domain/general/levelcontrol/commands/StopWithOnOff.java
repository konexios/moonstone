package com.arrow.selene.device.xbee.zcl.domain.general.levelcontrol.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class StopWithOnOff extends NoPayloadCommand {
	@Override
	protected int getId() {
		return LevelControlClusterCommands.STOP_WITH_ON_OFF_COMMAND_ID;
	}
}
