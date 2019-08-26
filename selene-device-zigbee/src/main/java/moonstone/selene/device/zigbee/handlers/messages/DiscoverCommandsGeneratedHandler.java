package moonstone.selene.device.zigbee.handlers.messages;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclClusterInfo;
import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.general.DiscoverCommandsGeneratedResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;
import moonstone.selene.device.zigbee.data.ClusterInfo;
import moonstone.selene.device.zigbee.data.CommandInfo;

public class DiscoverCommandsGeneratedHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		int clusterID = message.getClusterID();
		ZclClusterInfo zclCluster = ZclClusters.getCluster(clusterID);
		if (zclCluster == null) {
			logWarn(method, "Unsupported clusterId: 0x%02x", clusterID);
			return;
		}
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		ClusterInfo clusterInfo = endDeviceModule.getInfo().getZigbee()
		        .checkCreateEndpointCluster(message.getSourceEndpoint(), clusterID);
		DiscoverCommandsGeneratedResponse response = new DiscoverCommandsGeneratedResponse().extract(message.getData());
		byte[] commands = response.getCommands();
		logDebug(method, "commands size: %d", commands.length);
		if (commands.length > 0) {
			boolean fromServer = response.getHeader().isFromServer();
			for (byte id : commands) {
				Integer commandId = (int) id;
				String commandName = null;
				if (fromServer) {
					if (zclCluster.getGeneratedCommands() != null) {
						commandName = zclCluster.getGeneratedCommands().get(commandId);
					}
				} else {
					if (zclCluster.getReceivedCommands() != null) {
						ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>> pair = zclCluster
						        .getReceivedCommands().get(commandId);
						if (pair != null) {
							commandName = pair.getLeft();
						}
					}
				}
				if (StringUtils.isEmpty(commandName)) {
					commandName = "Unknown Command";
				}
				CommandInfo commandInfo = new CommandInfo(id, commandName);
				logInfo(method, "found generated command: %s of cluster: %s, direction: from %s", commandInfo,
				        clusterInfo, fromServer ? "server" : "client");
				clusterInfo.getGeneratedCommands().put(commandId, commandInfo);
			}
		}
	}
}
