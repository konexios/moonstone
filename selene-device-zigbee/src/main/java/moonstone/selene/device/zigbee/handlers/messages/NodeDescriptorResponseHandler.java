package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zdo.ActiveEndpointsRequest;
import moonstone.selene.device.xbee.zdo.NodeDescriptorResponse;
import moonstone.selene.device.xbee.zdo.ZdoCommands;
import moonstone.selene.device.xbee.zdo.ZdoConstants;
import moonstone.selene.device.zigbee.MessageInfo;
import moonstone.selene.device.zigbee.MessageType;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;

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
