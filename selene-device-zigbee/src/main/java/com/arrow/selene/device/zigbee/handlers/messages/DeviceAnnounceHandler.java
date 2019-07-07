package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zdo.DeviceAnnounceRequest;
import com.arrow.selene.device.xbee.zdo.DeviceAnnounceResponse;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;

public class DeviceAnnounceHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "DeviceAnnounceHandler.handle";
		DeviceAnnounceRequest request = DeviceAnnounceRequest.fromPayload(message.getData());
		XBee64BitAddress address = message.getDevice().get64BitAddress();
		XBee16BitAddress localAddress = request.getAddress16();
		logInfo(method, "device: %s has (re)joined the ZigBee network with local address: %s", address, localAddress);
		ZigBeeEndDeviceModule endDevice = module.getModule(address.toString());
		if (endDevice != null) {
			endDevice.setLocalAddress(localAddress.toString());
			logInfo(method, "set updated local address: %s", localAddress);
		}
		module.sendZdoMessage(address, ZdoCommands.END_DEVICE_ANNOUNCE_RSP, DeviceAnnounceResponse
		        .toPayload(request.getSequence(), localAddress, request.getAddress64(), request.getCaps()));
	}
}
