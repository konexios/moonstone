package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.data.ReadAttributeStatusRecord;
import com.arrow.selene.device.xbee.zcl.general.ReadAttributeResponse;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.AttributeInfo;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

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
