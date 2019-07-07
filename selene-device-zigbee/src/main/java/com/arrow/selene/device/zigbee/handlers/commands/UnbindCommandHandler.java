package com.arrow.selene.device.zigbee.handlers.commands;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.Loggable;
import com.arrow.selene.data.Device;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zdo.BindUnbindRequest;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.xbee.zdo.ZdoConstants;
import com.arrow.selene.device.zigbee.MessageInfo;
import com.arrow.selene.device.zigbee.MessageType;
import com.arrow.selene.device.zigbee.ZigBeeCoordinatorInfo;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceInfo;
import com.arrow.selene.device.zigbee.ZigBeeEndDeviceModule;
import com.arrow.selene.device.zigbee.data.BindUnbindPayload;
import com.arrow.selene.device.zigbee.data.BindingInfo;
import com.arrow.selene.device.zigbee.data.ClusterInfo;
import com.arrow.selene.device.zigbee.data.EndpointInfo;
import com.arrow.selene.device.zigbee.data.ZigBeeInfoHolder;
import com.arrow.selene.engine.Utils;
import com.arrow.selene.engine.service.DeviceService;
import com.digi.xbee.api.models.XBee64BitAddress;

public class UnbindCommandHandler extends Loggable implements CommandHandler {
	public static final String COMMAND = "unbind";
	private static final String PAYLOAD = "{\"srcEndpoint\":\"<number>\",\"dstUid\":\"<deviceUid>\","
	        + "\"dstEndpoint\":\"<number>\",\"clusterId\":\"<number>\"}";

	private ZigBeeEndDeviceModule module;

	public UnbindCommandHandler(ZigBeeEndDeviceModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "UnbindCommandHandler.handle";
		BindUnbindPayload unbind = JsonUtils.fromJson(payload, BindUnbindPayload.class);
		int srcEndpoint = unbind.getSrcEndpoint();
		EndpointInfo endpointInfo = module.getInfo().getZigbee().getEndpoints().get(srcEndpoint);
		int clusterId = unbind.getClusterId();
		boolean srcEndpointOutputPresent = false;
		boolean srcEndpointInputPresent = false;
		String clusterName = ZclClusters.getName(clusterId);
		if (endpointInfo != null) {
			ClusterInfo clusterInfo = endpointInfo.getClusters().get(clusterId);
			if (clusterInfo != null) {
				srcEndpointOutputPresent = !clusterInfo.getGeneratedCommands().isEmpty();
				srcEndpointInputPresent = !clusterInfo.getReceivedCommands().isEmpty();
			} else {
				logError(method, "unknown cluster: 0x%04x (%s)", clusterId, clusterName);
			}
		} else {
			logError(method, "unknown endpoint: %d", srcEndpoint);
		}
		boolean dstEndpointInputPresent = false;
		int dstEndpoint = unbind.getDstEndpoint();
		Device device = DeviceService.getInstance().findByTypeAndUid(ZigBeeCoordinatorInfo.DEFAULT_DEVICE_TYPE,
		        unbind.getDstUid());
		if (device != null) {
			logInfo(method, "coordinator found, skipping destination endpoint and cluster checks");
			String dstAddress = Utils.getProperties(device).getProperty("address");
			unbind(clusterId, srcEndpoint, module.getInfo().getUid(), dstEndpoint, unbind.getDstUid(), dstAddress);
		} else {
			device = DeviceService.getInstance().findByTypeAndUid(ZigBeeEndDeviceInfo.DEFAULT_DEVICE_TYPE,
			        unbind.getDstUid());
			if (device == null) {
				logWarn(method, "destination device not found, skipping unbinding");
				return;
			}
			logInfo(method, "end device found, checking destination endpoint");
			String dstAddress = Utils.getProperties(device).getProperty("address");
			ZigBeeInfoHolder zigbee = JsonUtils.fromJson(Utils.getProperties(device).getProperty("zigbee"),
			        ZigBeeInfoHolder.class);
			EndpointInfo dstEndpointInfo = null;
			if (zigbee != null) {
				dstEndpointInfo = zigbee.getEndpoints().get(dstEndpoint);
			}
			if (dstEndpointInfo != null) {
				ClusterInfo dstClusterInfo = dstEndpointInfo.getClusters().get(clusterId);
				if (dstClusterInfo != null) {
					dstEndpointInputPresent = !dstClusterInfo.getReceivedCommands().isEmpty();
				} else {
					logError(method, "cluster: 0x%04x (%s) is unknown on destination device", clusterId, clusterName);
				}
			} else {
				logError(method, "endpoint: %d is unknown on destination device", dstEndpoint);
			}
			if (!srcEndpointOutputPresent) {
				if (srcEndpointInputPresent) {
					logWarn(method, "unknown output cluster: 0x%04x (%s) on endpoint: %d", clusterId, clusterName,
					        srcEndpoint);
				} else {
					logWarn(method, "unknown cluster: 0x%04x (%s)", clusterId, clusterName);
				}
			} else if (!dstEndpointInputPresent) {
				logWarn(method, "destination device does not contain input cluster: 0x%04x (%s) in endpoint: %d",
				        clusterId, clusterName, dstEndpoint);
			}
			unbind(clusterId, srcEndpoint, module.getInfo().getUid(), dstEndpoint, unbind.getDstUid(), dstAddress);
		}
	}

	private void unbind(int clusterId, int srcEndpoint, String srcUid, int dstEndpoint, String dstUid,
	        String dstAddress) {
		String method = "unbind";
		logInfo(method, "unbinding cluster: 0x%04x (%s) on endpoint: %d of device: %s from endpoint: %d of device: %s",
		        clusterId, ZclClusters.getName(clusterId), srcEndpoint, srcUid, dstEndpoint, dstUid);
		XBee64BitAddress source = new XBee64BitAddress(module.getInfo().getAddress());
		XBee64BitAddress destination = new XBee64BitAddress(dstAddress);
		byte sequence = module.nextSequence();
		module.addMessage(new MessageInfo(sequence, MessageType.ZDO_MESSAGE, module.getInfo().getAddress(),
		        ZdoConstants.ZDO_DST_ENDPOINT, ZdoCommands.UNBIND_REQ,
		        BindUnbindRequest.toPayload(sequence, source, srcEndpoint, clusterId, destination, dstEndpoint),
		        "UNBIND_REQ", ZdoCommands.UNBIND_RSP));
		module.getInfo().getBindings().removeBinding(new BindingInfo().withSrcEndpoint(srcEndpoint)
		        .withClusterId(clusterId).withDstAddress(destination.toString()).withDstEndpoint(dstEndpoint));
		module.persistUpdatedDeviceInfo();
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
