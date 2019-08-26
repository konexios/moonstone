package moonstone.selene.device.zigbee.handlers.commands;

import java.util.Collections;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.acs.JsonUtils;
import moonstone.selene.Loggable;
import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.ZclClusterInfo;
import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.device.xbee.zcl.data.AttributeReportingConfigRecord;
import moonstone.selene.device.xbee.zcl.general.ConfigureReportingRequest;
import moonstone.selene.device.xbee.zcl.general.HaProfileCommands;
import moonstone.selene.device.zigbee.MessageInfo;
import moonstone.selene.device.zigbee.MessageType;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceInfo;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;
import moonstone.selene.device.zigbee.data.AttributeInfo;
import moonstone.selene.device.zigbee.data.ClusterInfo;
import moonstone.selene.device.zigbee.data.ConfigureReportingPayload;
import moonstone.selene.device.zigbee.data.EndpointInfo;

public class ConfigureReportingCommandHandler extends Loggable implements CommandHandler {
	public static final String COMMAND = "configureReporting";
	private static final String PAYLOAD = "{\"endpoint\":\"<number>\",\"clusterId\":\"<number>\","
	        + "\"attributeId\":\"<number>\",\"min\":\"<number>\",\"max\":\"<number>\"}";

	private ZigBeeEndDeviceModule module;

	public ConfigureReportingCommandHandler(ZigBeeEndDeviceModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "ConfigureReportingCommandHandler.handle";
		ConfigureReportingPayload configureReporting = JsonUtils.fromJson(payload, ConfigureReportingPayload.class);
		int clusterId = configureReporting.getClusterId();
		ZclClusterInfo cluster = ZclClusters.getCluster(clusterId);
		String clusterName = ZclClusters.getName(clusterId);
		if (cluster == null) {
			logWarn(method, "unknown cluster: 0x%04x (%s)", clusterId, clusterName);
			return;
		}
		int attributeId = configureReporting.getAttributeId();
		ImmutablePair<String, Attribute<? extends SensorData<?>>> attribute = cluster.getAttributes().get(attributeId);
		if (attribute == null) {
			logWarn(method, "unknown attribute: 0x%04x in cluster: 0x%04x (%s)", attributeId, clusterId,
			        ZclClusters.getName(clusterId));
			return;
		}
		int endpoint = configureReporting.getEndpoint();
		ZigBeeEndDeviceInfo info = module.getInfo();
		EndpointInfo endpointInfo = info.getZigbee().getEndpoints().get(endpoint);
		if (endpointInfo == null) {
			logWarn(method, "device has no endpoint: %d", endpoint);
			return;
		}
		ClusterInfo clusterInfo = endpointInfo.getClusters().get(clusterId);
		if (clusterInfo == null) {
			logWarn(method, "device has no cluster :0x%04x(%s) on endpoint: %d", clusterId, clusterName, endpoint);
			return;
		}
		AttributeInfo attributeInfo = clusterInfo.getAttributes().get(attributeId);
		if (attributeInfo == null) {
			logWarn(method, "device does not support attribute: %s of cluster: %s", attributeInfo, clusterInfo);
			return;
		}
		AttributeReportingConfigRecord record = new AttributeReportingConfigRecord();
		record.setAttributeId(attributeId);
		record.setAttributeDataType(attributeInfo.getAttributeDataType());
		record.setMinimumReportingInterval(configureReporting.getMin());
		record.setMaximumReportingInterval(configureReporting.getMax());
		byte[] value = new byte[record.getAttributeDataType().getLength()];
		value[0] = 1;
		record.setReportableChange(value);
		logInfo(method, "configure reporting of attribute: %s of cluster: %s", attributeInfo, clusterInfo);
		byte sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, info.getAddress(), endpoint,
		        clusterId, new ConfigureReportingRequest(info.getManufacturerCode(), Collections.singletonList(record))
		                .build(sequence, clusterId),
		        "CONFIGURE_REPORTING_REQ", HaProfileCommands.CONFIGURE_REPORTING_RSP));
		// module.sendMessage(new XBee64BitAddress(info.getAddress()), endpoint,
		// clusterId, ZclConstants.HA_PROFILE_ID,
		// new ConfigureReportingRequest(info.getManufacturerCode(),
		// Collections.singletonList(record))
		// .build(clusterId));
	}

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getPayload() {
		return PAYLOAD;
	}
}
