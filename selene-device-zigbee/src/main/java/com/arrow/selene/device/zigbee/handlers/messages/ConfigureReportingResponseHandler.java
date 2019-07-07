package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.data.AttributeStatusRecord;
import com.arrow.selene.device.xbee.zcl.general.ConfigureReportingResponse;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.AttributeInfo;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class ConfigureReportingResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "ConfigureReportingResponseHandler.handle";
		ConfigureReportingResponse response = new ConfigureReportingResponse().extract(message.getData());
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		ClusterInfo clusterInfo = endDeviceModule.getInfo().getZigbee().checkCreateEndpointCluster(
				message.getSourceEndpoint(), message.getClusterID());
		if (response.getStatuses().isEmpty()) {
			logInfo(method, "device: %s reports about successful reporting configuration",
					message.getDevice().get64BitAddress());
			return;
		}
		for (AttributeStatusRecord record : response.getStatuses()) {
			Integer attributeId = record.getAttributeId();
			AttributeInfo attributeInfo = clusterInfo.getAttributes().get(attributeId);
			if (attributeInfo == null) {
				logWarn(method, "attribute: 0x%02x not found in cluster: %s", attributeId, clusterInfo);
				logError(method,
						"attribute: 0x%02x of cluster: %s was not configured for reporting with status: 0x%02x ",
						attributeId, clusterInfo, record.getStatus());
			} else {
				logError(method, "attribute: %s of cluster: %s was not configured for reporting with status: 0x%02x ",
						attributeInfo, clusterInfo, record.getStatus());
			}
		}
		endDeviceModule.persistUpdatedDeviceInfo();
	}
}
