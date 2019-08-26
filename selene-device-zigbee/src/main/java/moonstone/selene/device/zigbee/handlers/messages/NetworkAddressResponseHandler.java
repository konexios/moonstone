package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zdo.NetworkAddressResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

public class NetworkAddressResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		NetworkAddressResponse response = NetworkAddressResponse.fromPayload(message.getData());
		logInfo(method, "64bitAddr: %s, 16bitAddr: %s", response.getAddress64().toString(),
		        response.getAddress16().toString());
	}
}
