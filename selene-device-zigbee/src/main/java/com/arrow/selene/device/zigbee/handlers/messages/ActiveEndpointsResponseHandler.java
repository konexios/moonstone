package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zdo.ActiveEndpointsResponse;
import com.arrow.selene.device.xbee.zdo.SimpleDescriptorRequest;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.xbee.zdo.ZdoConstants;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;

public class ActiveEndpointsResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "ActiveEndpointsResponseHandler.handle";
		ActiveEndpointsResponse response = ActiveEndpointsResponse.fromPayload(message.getData());
		if (response.getStatus() == ZclStatus.SUCCESS) {
			XBee16BitAddress address = response.getAddress();
			ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
			for (byte endpoint : response.getEndpoints()) {
				byte sequence = endDeviceModule.nextSequence();
				endDeviceModule.addMessage(new MessageInfo(sequence, MessageType.ZDO_MESSAGE,
				        message.getDevice().get64BitAddress().toString(), ZdoConstants.ZDO_DST_ENDPOINT,
				        ZdoCommands.SIMPLE_DESCRIPTOR_REQ,
				        SimpleDescriptorRequest.toPayload(sequence, address, endpoint), "SIMPLE_DESCRIPTOR_REQ",
				        ZdoCommands.SIMPLE_DESCRIPTOR_RSP));
			}
		} else {
			logWarn(method, "received response with status: 0x%02x", response.getStatus());
		}
	}
}
