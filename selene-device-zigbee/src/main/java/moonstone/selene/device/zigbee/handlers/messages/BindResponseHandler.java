package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zdo.BindResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

public class BindResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "BindResponseHandler.handle";
		BindResponse response = BindResponse.fromPayload(message.getData());
		if (response.getStatus() == ZclStatus.SUCCESS) {
			logInfo(method, "device: %s reports about successful binding", message.getDevice().get64BitAddress());
		} else {
			logWarn(method, "device: %s reports about failed binding with status: 0x%02x", response.getStatus());
		}
	}
}
