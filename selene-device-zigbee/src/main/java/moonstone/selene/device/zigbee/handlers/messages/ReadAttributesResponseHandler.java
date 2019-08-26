package moonstone.selene.device.zigbee.handlers.messages;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.data.ReadAttributeStatusRecord;
import moonstone.selene.device.xbee.zcl.general.ReadAttributeResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;
import moonstone.selene.device.zigbee.data.AttributeInfo;
import moonstone.selene.device.zigbee.data.ClusterInfo;

public class ReadAttributesResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "ReadAttributesResponseHandler.handle";
		ReadAttributeResponse response = new ReadAttributeResponse().extract(message.getData());
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		ClusterInfo clusterInfo = endDeviceModule.getInfo().getZigbee()
		        .checkCreateEndpointCluster(message.getSourceEndpoint(), message.getClusterID());
		for (ReadAttributeStatusRecord record : response.getRecords()) {
			Integer attributeId = (int) record.getAttributeId();
			AttributeInfo attributeInfo = clusterInfo.getAttributes().get(attributeId);
			if (attributeInfo == null) {
				logError(method, "attribute: 0x%04x not found in cluster: %s", attributeId, clusterInfo);
			} else {
				attributeInfo.setValue(record.getStringValue());
			}
		}
	}
}
