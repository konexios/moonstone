package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zdo.NetworkAddressResponse;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class NetworkAddressResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		NetworkAddressResponse response = NetworkAddressResponse.fromPayload(message.getData());
		logInfo(method, "64bitAddr: %s, 16bitAddr: %s", response.getAddress64().toString(),
		        response.getAddress16().toString());
	}
}
