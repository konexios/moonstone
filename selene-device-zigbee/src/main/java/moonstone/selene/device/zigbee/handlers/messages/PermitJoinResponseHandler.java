package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zdo.PermitJoinResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

public class PermitJoinResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "PermitJoinResponseHandler.handle";
		PermitJoinResponse response = PermitJoinResponse.fromPayload(message.getData());
		logInfo(method, "received permit join response with status: 0x%02x from device: %s",
				message.getDevice().get64BitAddress(), response.getStatus());
	}
}
