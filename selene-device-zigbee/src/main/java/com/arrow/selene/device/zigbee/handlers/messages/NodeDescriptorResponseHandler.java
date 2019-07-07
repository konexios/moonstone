package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zdo.ActiveEndpointsRequest;
import com.arrow.selene.device.xbee.zdo.NodeDescriptorResponse;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.xbee.zdo.ZdoConstants;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class NodeDescriptorResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule coordinator) {
		String method = "NodeDescriptorResponseHandler.handle";
		NodeDescriptorResponse response = NodeDescriptorResponse.fromPayload(message.getData());
		if (response.getStatus() == ZclStatus.SUCCESS) {
			ZigBeeEndDeviceModule module = coordinator.getModule(message.getDevice().get64BitAddress().toString());
			module.getInfo().setRouter(response.isRouter());
			module.getInfo().setManufacturerCode(response.getManufacturerCode());
			byte sequence = module.nextSequence();
			module.addMessage(new MessageInfo(sequence, MessageType.ZDO_MESSAGE, module.getInfo().getAddress(),
			        ZdoConstants.ZDO_DST_ENDPOINT, ZdoCommands.ACTIVE_ENDPOINTS_REQ,
			        ActiveEndpointsRequest.toPayload(sequence, response.getAddress()), "ACTIVE_ENDPOINTS_REQ",
			        ZdoCommands.ACTIVE_ENDPOINTS_RSP));
		} else {
			logWarn(method, "received response with status: 0x%02x", response.getStatus());
		}
	}
}
