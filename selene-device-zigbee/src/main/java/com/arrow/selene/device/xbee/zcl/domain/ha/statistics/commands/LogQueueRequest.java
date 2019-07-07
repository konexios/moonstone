package com.arrow.selene.device.xbee.zcl.domain.ha.statistics.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class LogQueueRequest extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceStatisticsClusterCommands.LOG_QUEUE_REQUEST_COMMAND_ID;
	}
}
