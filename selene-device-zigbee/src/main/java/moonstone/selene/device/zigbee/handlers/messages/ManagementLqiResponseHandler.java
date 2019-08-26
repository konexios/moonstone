package moonstone.selene.device.zigbee.handlers.messages;

import java.util.Objects;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zdo.ManagementLqiRequest;
import moonstone.selene.device.xbee.zdo.ManagementLqiResponse;
import moonstone.selene.device.xbee.zdo.ZdoCommands;
import moonstone.selene.device.xbee.zdo.data.DeviceType;
import moonstone.selene.device.xbee.zdo.data.Neighbor;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeModuleAbstract;

public class ManagementLqiResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule coordinator) {
		String method = "ManagementLqiResponseHandler.handle";
		String address = message.getDevice().get64BitAddress().toString();
		ZigBeeModuleAbstract<?, ?, ?, ?> module = Objects.equals(address, coordinator.getInfo().getAddress())
		        ? coordinator : coordinator.getModule(address);
		if (module == null) {
			logWarn(method, "device: %s not found", address);
			return;
		}
		coordinator.addNeighbor(address);
		ManagementLqiResponse response = ManagementLqiResponse.fromPayload(message.getData());
		if (response.getStartIndex() == 0) {
			for (Neighbor neighbor : module.getNeighbors().values()) {
				neighbor.withLqi(0).withOn(false);
			}
		}
		for (Neighbor neighbor : response.getNeighbors()) {
			String neighborAddress = neighbor.getExpandedAddress().toString();
			module.getNeighbors().put(neighborAddress, neighbor.withOn(true));
			if (neighbor.getDeviceType() != DeviceType.END_DEVICE && !coordinator.hasNeighbor(neighborAddress)) {
				coordinator.sendZdoMessage(neighbor.getExpandedAddress(), ZdoCommands.MANAGEMENT_LQI_REQ,
				        ManagementLqiRequest.toPayload(coordinator.nextSequence(), 0));
			}
		}
		int polled = response.getStartIndex() + response.getNeighbors().length;
		if (polled < response.getTotalNumberOfEntries()) {
			coordinator.sendZdoMessage(message.getDevice(), ZdoCommands.MANAGEMENT_LQI_REQ,
			        ManagementLqiRequest.toPayload(coordinator.nextSequence(), polled));
		} else {
			module.publishHealthData();
		}
	}
}
