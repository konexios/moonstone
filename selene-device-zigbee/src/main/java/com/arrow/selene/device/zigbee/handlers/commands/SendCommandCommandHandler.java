package com.arrow.selene.device.zigbee.handlers.commands;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclClusterInfo;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.general.HaProfileCommands;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.arrow.selene.device.zigbee.data.CommandInfo;
import com.arrow.selene.device.zigbee.data.EndpointInfo;
import com.arrow.selene.device.zigbee.data.SendCommandPayload;
import com.digi.xbee.api.models.XBee64BitAddress;

public class SendCommandCommandHandler extends Loggable implements CommandHandler {
	public static final String COMMAND = "sendCommand";

	private ZigBeeEndDeviceModule module;

	public SendCommandCommandHandler(ZigBeeEndDeviceModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "handle";
		SendCommandPayload sendCommand = JsonUtils.fromJson(payload, SendCommandPayload.class);
		int clusterId = sendCommand.getClusterId();
		ZclClusterInfo cluster = ZclClusters.getCluster(clusterId);
		String clusterName = ZclClusters.getName(clusterId);
		if (cluster == null) {
			logWarn(method, "unknown cluster: 0x%04x (%s)", clusterId, clusterName);
			return;
		}
		int commandId = sendCommand.getCommandId();
		ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>> pair = cluster.getReceivedCommands()
		        .get(commandId);
		if (pair == null) {
			logWarn(method, "unknown command: 0x%02x in cluster: 0x%04x (%s)", commandId, clusterId, clusterName);
			return;
		}
		int dstEndpoint = sendCommand.getDstEndpoint();
		EndpointInfo endpointInfo = module.getInfo().getZigbee().getEndpoints().get(dstEndpoint);
		if (endpointInfo == null) {
			logWarn(method, "device has no endpoint: %d", dstEndpoint);
			return;
		}
		ClusterInfo clusterInfo = endpointInfo.getClusters().get(clusterId);
		if (clusterInfo == null) {
			logWarn(method, "device has no cluster: 0x%04x (%s) on endpoint: %d", clusterId, clusterName, dstEndpoint);
			return;
		}
		CommandInfo commandInfo = clusterInfo.getReceivedCommands().get(commandId);
		if (commandInfo == null) {
			logWarn(method, "device does not support command: 0x%02x (%s) of cluster: %s on endpoint: %d", commandId,
			        pair.getLeft(), clusterInfo, dstEndpoint);
			return;
		}
		XBee64BitAddress address = new XBee64BitAddress(module.getInfo().getAddress());
		module.logInfo(method, "sending command: %s of cluster: %s to device: %s", commandInfo, clusterInfo, address);
		try {
			byte sequence = module.nextSequence();
			byte[] data = JsonUtils.fromJson(payload, pair.getRight()).build(sequence, clusterId);
			// int manufacturerShift = (data[0] & 0x04) == 0 ? 0 : 2;
			// module.addMessage(new MessageInfo(MessageType.HA_PROFILE_MESSAGE,
			// module.getInfo().getAddress(),
			// dstEndpoint, clusterId, data, "DEFAULT_REQ",
			// new ExpectedResponse(data[1 + manufacturerShift],
			// HaProfileCommands.DEFAULT_RSP)));
			module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, module.getInfo().getAddress(),
			        dstEndpoint, clusterId, data, "DEFAULT_REQ", HaProfileCommands.DEFAULT_RSP));

		} catch (Exception e) {
			logError(method, "cannot build command payload, check command parameters", e);
		}
	}

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getPayload() {
		return "";
	}
}
