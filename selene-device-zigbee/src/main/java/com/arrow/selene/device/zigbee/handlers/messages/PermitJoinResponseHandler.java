package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zdo.PermitJoinResponse;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class PermitJoinResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "PermitJoinResponseHandler.handle";
		PermitJoinResponse response = PermitJoinResponse.fromPayload(message.getData());
		logInfo(method, "received permit join response with status: 0x%02x from device: %s",
				message.getDevice().get64BitAddress(), response.getStatus());
	}
}
