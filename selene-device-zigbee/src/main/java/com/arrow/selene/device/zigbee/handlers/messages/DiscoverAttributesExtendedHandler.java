package com.arrow.selene.device.zigbee.handlers.messages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.ZclClusterInfo;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.arrow.selene.device.xbee.zcl.data.AttributeExtendedRecord;
import com.arrow.selene.device.xbee.zcl.general.DiscoverAttributesExtendedRequest;
import com.arrow.selene.device.xbee.zcl.general.DiscoverAttributesExtendedResponse;
import com.arrow.selene.device.xbee.zcl.general.HaProfileCommands;
import com.arrow.selene.device.xbee.zcl.general.ReadAttributesRequest;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.AccessType;
import com.arrow.selene.device.zigbee.data.AttributeInfo;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class DiscoverAttributesExtendedHandler extends Loggable implements MessageHandler {

	public static final int DEFAULT_CHUNK_SIZE = 5;

	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		DiscoverAttributesExtendedResponse response = new DiscoverAttributesExtendedResponse()
		        .extract(message.getData());
		int clusterID = message.getClusterID();
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		int manufacturerCode = endDeviceModule.getInfo().getManufacturerCode();

		List<AttributeExtendedRecord> records = response.getAttributes();
		if (!response.isDiscoveryComplete()) {
			int attributeId = records.get(records.size() - 1).getAttributeId() + 1;
			DiscoverAttributesExtendedRequest dae = new DiscoverAttributesExtendedRequest(manufacturerCode, attributeId,
			        10);
			logInfo(method, "discovering additional attributes of cluster: 0x%04x(%s) starting from 0x%04x", clusterID,
			        ZclClusters.getName(clusterID), attributeId);
			byte sequence = module.nextSequence();
			byte[] payload = dae.build(sequence, clusterID);
			endDeviceModule.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE,
			        message.getDevice().get64BitAddress().toString(), message.getSourceEndpoint(), clusterID, payload,
			        "DISCOVER_ATTRIBUTES_EXTENDED_REQ", HaProfileCommands.DISCOVER_ATTRIBUTES_EXTENDED_RSP));
		}
		if (!records.isEmpty()) {
			ClusterInfo clusterInfo = endDeviceModule.getInfo().getZigbee()
			        .checkCreateEndpointCluster(message.getSourceEndpoint(), clusterID);
			List<Integer> readAttributes = new ArrayList<>();
			ZclClusterInfo zclCluster = ZclClusters.getCluster(clusterID);
			for (AttributeExtendedRecord record : records) {
				Integer attributeId = record.getAttributeId();
				AttributeInfo attributeInfo = clusterInfo.getAttributes().get(attributeId);
				if (attributeInfo == null) {
					String attributeName = null;
					if (zclCluster != null) {
						ImmutablePair<String, Attribute<? extends SensorData<?>>> pair = zclCluster.getAttributes()
						        .get(attributeId);
						if (pair != null) {
							attributeName = pair.getLeft();
						}
					}
					if (StringUtils.isEmpty(attributeName)) {
						attributeName = "Unknown attribute";
					}
					attributeInfo = new AttributeInfo(attributeId, record.getAttributeDataType(), attributeName,
					        AccessType.values()[record.getAccessType()]);
					clusterInfo.getAttributes().put(attributeId, attributeInfo);
					logInfo(method, "created attribute: %s of cluster: %s", attributeInfo, clusterInfo);
					readAttributes.add(attributeId);
				} else {
					logInfo(method, "attribute: %s exists in cluster: %s", attributeInfo, clusterInfo);
				}
			}

			// only read attributes for BASIC cluster
			if (clusterID == ZclClusters.BASIC) {
				if (!readAttributes.isEmpty()) {
					int size = readAttributes.size();
					int start = 0;
					int end = 0;
					while (start < size) {
						end += DEFAULT_CHUNK_SIZE;
						if (end > size) {
							end = size;
						}
						int[] ids = new int[end - start];
						for (int i = start; i < end; i++) {
							ids[i - start] = readAttributes.get(i);
						}
						start = end;
						ReadAttributesRequest ra = new ReadAttributesRequest(manufacturerCode, ids);
						logInfo(method, "reading values of %d attributes of cluster: %s", ids.length, clusterInfo);
						byte sequence = module.nextSequence();
						byte[] payload = ra.build(sequence, clusterID);
						// int sequenceIndex = ZclClusters.getCluster(clusterID)
						// == null ? 3 : 1;
						endDeviceModule.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE,
						        endDeviceModule.getInfo().getAddress(), message.getSourceEndpoint(), clusterID, payload,
						        "READ_ATTRIBUTES", HaProfileCommands.READ_ATTRIBUTES_RSP));
					}
				}
			}
		}
	}
}
