package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zdo.DeviceAnnounceRequest;
import moonstone.selene.device.xbee.zdo.DeviceAnnounceResponse;
import moonstone.selene.device.xbee.zdo.ZdoCommands;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;

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
