package moonstone.selene.device.zigbee.handlers.messages;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclClusterInfo;
import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.general.DiscoverCommandsReceivedResponse;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;
import moonstone.selene.device.zigbee.data.ClusterInfo;
import moonstone.selene.device.zigbee.data.CommandInfo;

public class DiscoverCommandsReceivedHandler extends Loggable implements MessageHandler {
	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule module) {
		String method = "handle";
		DiscoverCommandsReceivedResponse response = new DiscoverCommandsReceivedResponse().extract(message.getData());
		int clusterID = message.getClusterID();
		ZclClusterInfo zclCluster = ZclClusters.getCluster(clusterID);
		if (zclCluster == null) {
			logWarn(method, "Unsupported clusterId: 0x%02x", clusterID);
			return;
		}
		ZigBeeEndDeviceModule endDeviceModule = module.getModule(message.getDevice().get64BitAddress().toString());
		ClusterInfo clusterInfo = endDeviceModule.getInfo().getZigbee()
		        .checkCreateEndpointCluster(message.getSourceEndpoint(), clusterID);
		byte[] commands = response.getCommands();
		logDebug(method, "commands size: %d", commands.length);
		if (commands.length > 0) {
			for (byte id : commands) {
				boolean fromServer = response.getHeader().isFromServer();
				int commandId = Byte.toUnsignedInt(id);
				String commandName = null;
				if (fromServer) {
					if (zclCluster.getReceivedCommands() != null) {
						ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>> pair = zclCluster
						        .getReceivedCommands().get(commandId);
						if (pair != null) {
							commandName = pair.getLeft();
						}
					}
				} else if (zclCluster.getGeneratedCommands() != null) {
					commandName = zclCluster.getGeneratedCommands().get(commandId);
				}
				if (StringUtils.isEmpty(commandName)) {
					commandName = "Unknown Command";
				}
				CommandInfo commandInfo = new CommandInfo(id, commandName);
				logInfo(method, "found received command: %s of cluster: %s, direction: from %s", commandInfo,
				        clusterInfo, fromServer ? "server" : "client");
				clusterInfo.getReceivedCommands().put(commandId, commandInfo);
			}
		}
	}
}
