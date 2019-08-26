package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zdo.BindResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

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
