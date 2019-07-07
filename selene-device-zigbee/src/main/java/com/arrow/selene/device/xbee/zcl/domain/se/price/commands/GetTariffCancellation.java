package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class GetTariffCancellation extends NoPayloadCommand {
	@Override
	protected int getId() {
		return PriceClusterCommands.GET_TARIFF_CANCELLATION_COMMAND_ID;
	}
}
