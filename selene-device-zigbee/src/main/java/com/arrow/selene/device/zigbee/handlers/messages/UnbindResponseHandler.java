package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zdo.BindResponse;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class UnbindResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "UnbindResponseHandler.handle";
		BindResponse response = BindResponse.fromPayload(message.getData());
		if (response.getStatus() == ZclStatus.SUCCESS) {
			logInfo(method, "device: %s reports about successful unbinding", message.getDevice().get64BitAddress());
		} else {
			logWarn(method, "device: %s reports about failed unbinding with status: 0x%02x",
					message.getDevice().get64BitAddress(), response.getStatus());
		}
	}
}
