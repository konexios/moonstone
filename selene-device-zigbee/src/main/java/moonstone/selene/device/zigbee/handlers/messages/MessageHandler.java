package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

public interface MessageHandler {
	void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module);
}
