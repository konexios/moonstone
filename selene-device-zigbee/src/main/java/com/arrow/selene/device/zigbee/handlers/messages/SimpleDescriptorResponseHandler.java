package com.arrow.selene.device.zigbee.handlers.messages;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.xbee.zcl.ApplicationProfiles;
import com.arrow.selene.device.xbee.zcl.HaDeviceProfiles;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zcl.ZllDeviceProfiles;
import com.arrow.selene.device.xbee.zcl.general.DiscoverAttributesExtendedRequest;
import com.arrow.selene.device.xbee.zcl.general.DiscoverCommandsGeneratedRequest;
import com.arrow.selene.device.xbee.zcl.general.DiscoverCommandsReceivedRequest;
import com.arrow.selene.device.xbee.zcl.general.GeneralRequest;
import com.arrow.selene.device.xbee.zcl.general.HaProfileCommands;
import com.arrow.selene.device.xbee.zdo.SimpleDescriptorResponse;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.digi.xbee.api.models.ExplicitXBeeMessage;

public class SimpleDescriptorResponseHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		SimpleDescriptorResponse response = SimpleDescriptorResponse.fromPayload(message.getData());
		if (response.getStatus() == ZclStatus.SUCCESS) {
			int applicationProfile = response.getApplicationProfile();
			ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
			switch (applicationProfile) {
			case ApplicationProfiles.HOME_AUTOMATION_PROFILE: {
				process(endDeviceModule, response, applicationProfile,
				        HaDeviceProfiles.getName(response.getDeviceProfile()));
				break;
			}
			case ApplicationProfiles.LIGHT_LINK_PROFILE: {
				process(endDeviceModule, response, applicationProfile,
				        ZllDeviceProfiles.getName(response.getDeviceProfile()));
				break;
			}
			default: {
				process(endDeviceModule, response, applicationProfile,
				        String.format("0x%04x", response.getDeviceProfile()));
				break;
			}
			}
		} else {
			logWarn(method, "received response with status: 0x%02x", response.getStatus());
		}
	}

	private void process(ZigBeeEndDeviceModule module, SimpleDescriptorResponse response, int applicationProfile,
	        String deviceProfileName) {
		String method = "process";

		int endpoint = response.getEndpoint();
		module.getInfo().getZigbee().checkCreateEndpoint(endpoint)
		        .withApplicationProfile(ApplicationProfiles.getName(applicationProfile))
		        .withDeviceProfile(deviceProfileName);
		for (int clusterId : response.getInputClusters()) {
			ClusterInfo info = module.getInfo().getZigbee().checkCreateEndpointCluster(endpoint, clusterId);
			logDebug(method, "added new clusterInfo: %s", info.getName());
		}
		for (int clusterId : response.getOutputClusters()) {
			ClusterInfo info = module.getInfo().getZigbee().checkCreateEndpointCluster(endpoint, clusterId);
			logDebug(method, "added new clusterInfo: %s", info.getName());
		}
		module.persistUpdatedDeviceInfo();

		int manufacturerCode = module.getInfo().getManufacturerCode();
		DiscoverAttributesExtendedRequest dae = new DiscoverAttributesExtendedRequest(manufacturerCode, 0, 10);
		GeneralRequest dcrServer = new DiscoverCommandsReceivedRequest(manufacturerCode, false, 0, 60);
		GeneralRequest dcgServer = new DiscoverCommandsGeneratedRequest(manufacturerCode, false, 0, 60);
		GeneralRequest dcrClient = new DiscoverCommandsReceivedRequest(manufacturerCode, true, 0, 60);
		GeneralRequest dcgClient = new DiscoverCommandsGeneratedRequest(manufacturerCode, true, 0, 60);
		String address = module.getInfo().getAddress();
		for (int clusterId : response.getInputClusters()) {
			logInfo(method, "discovering attributes and commands of input cluster: 0x%04x (%s)", clusterId,
			        ZclClusters.getName(clusterId));
			discover(module, endpoint, address, dae, dcrServer, dcgServer, dcrClient, dcgClient, clusterId);
		}
		for (int clusterId : response.getOutputClusters()) {
			logInfo(method, "discovering attributes and commands of output cluster: 0x%04x (%s)", clusterId,
			        ZclClusters.getName(clusterId));
			discover(module, endpoint, address, dae, dcrServer, dcgServer, dcrClient, dcgClient, clusterId);
		}

		module.persistUpdatedDeviceInfo();
	}

	private void discover(ZigBeeEndDeviceModule module, int endpoint, String address,
	        DiscoverAttributesExtendedRequest dae, GeneralRequest dcrServer, GeneralRequest dcgServer,
	        GeneralRequest dcrClient, GeneralRequest dcgClient, int clusterId) {
		byte sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, address, endpoint, clusterId,
		        dae.build(sequence, clusterId), "DISCOVER_ATTRIBUTES_EXTENDED_REQ",
		        HaProfileCommands.DISCOVER_ATTRIBUTES_EXTENDED_RSP));

		sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, address, endpoint, clusterId,
		        dcrServer.build(sequence, clusterId), "DISCOVER_COMMANDS_RECEIVED_REQ",
		        HaProfileCommands.DISCOVER_COMMANDS_RECEIVED_RSP));

		sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, address, endpoint, clusterId,
		        dcgServer.build(sequence, clusterId), "DISCOVER_COMMANDS_GENERATED_REQ",
		        HaProfileCommands.DISCOVER_COMMANDS_GENERATED_RSP));

		sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, address, endpoint, clusterId,
		        dcrClient.build(sequence, clusterId), "DISCOVER_COMMANDS_RECEIVED_REQ",
		        HaProfileCommands.DISCOVER_COMMANDS_RECEIVED_RSP));

		sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.HA_PROFILE_MESSAGE, address, endpoint, clusterId,
		        dcgClient.build(sequence, clusterId), "DISCOVER_COMMANDS_GENERATED_REQ",
		        HaProfileCommands.DISCOVER_COMMANDS_GENERATED_RSP));
	}
}
