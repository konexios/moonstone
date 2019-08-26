package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zdo.ActiveEndpointsResponse;
import moonstone.selene.device.xbee.zdo.SimpleDescriptorRequest;
import moonstone.selene.device.xbee.zdo.ZdoCommands;
import moonstone.selene.device.xbee.zdo.ZdoConstants;
import moonstone.selene.device.zigbee.MessageInfo;
import moonstone.selene.device.zigbee.MessageType;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;

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
