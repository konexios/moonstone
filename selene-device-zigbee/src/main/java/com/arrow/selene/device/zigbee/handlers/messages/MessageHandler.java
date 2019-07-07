package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public interface MessageHandler {
	void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module);
}
