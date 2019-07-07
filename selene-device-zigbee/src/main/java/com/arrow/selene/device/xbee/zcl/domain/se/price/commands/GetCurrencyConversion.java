package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import com.arrow.selene.device.xbee.zcl.NoPayloadCommand;

public class GetCurrencyConversion extends NoPayloadCommand {
	@Override
	protected int getId() {
		return PriceClusterCommands.GET_CURRENCY_CONVERSION_COMMAND_COMMAND_ID;
	}
}
