package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class StopMoveStep extends NoPayloadCommand {
	@Override
	protected int getId() {
		return LightingColorClusterCommands.STOP_MOVE_STEP_COMMAND_ID;
	}
}
