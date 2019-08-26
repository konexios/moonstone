package moonstone.selene.device.zigbee.handlers.messages;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.digi.xbee.api.models.ExplicitXBeeMessage;

import moonstone.selene.Loggable;
import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclClusterInfo;
import moonstone.selene.device.xbee.zcl.ZclClusters;
import moonstone.selene.device.xbee.zcl.ZclDataType;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zcl.general.DefaultResponse;
import moonstone.selene.device.xbee.zcl.general.HaProfileCommands;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;
import moonstone.selene.device.zigbee.data.AccessType;
import moonstone.selene.device.zigbee.data.AttributeInfo;
import moonstone.selene.device.zigbee.data.ClusterInfo;
import moonstone.selene.device.zigbee.data.CommandInfo;

public class DefaultResponseHandler extends Loggable implements MessageHandler {
	static final ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>> DEFAULT_VALUE = new ImmutablePair<>(
	        CommandInfo.UNKNOWN_COMMAND, null);

	@Override
	public void handle(ExplicitXBeeMessage message, ZigBeeCoordinatorModule coordinator) {
		String method = "DefaultResponseHandler.handle";
		DefaultResponse response = new DefaultResponse().extract(message.getData());
		int commandId = response.getCommandId();
		String commandName = HaProfileCommands.getName(commandId);
		int statusCode = response.getStatusCode();
		int clusterId = message.getClusterID();
		String clusterName = ZclClusters.getName(clusterId);
		String address = message.getDevice().get64BitAddress().toString();
		String status = ZclStatus.getName(statusCode);

		logInfo(method,
		        "SECOND: device: %s responses with default response on command: 0x%02x (%s) of cluster: 0x%04x (%s) "
		                + "with status: 0x%02x (%s)",
		        address, commandId, commandName, clusterId, clusterName, statusCode, status);

		if (statusCode == ZclStatus.UNSUP_GENERAL_COMMAND) {
			ZigBeeEndDeviceModule module = coordinator.getModule(message.getDevice().get64BitAddress().toString());
			ZclClusterInfo zclClusterInfo = ZclClusters.getCluster(clusterId);
			ClusterInfo clusterInfo = module.getInfo().getZigbee()
			        .checkCreateEndpointCluster(message.getSourceEndpoint(), clusterId);
			if (commandId == HaProfileCommands.DISCOVER_COMMANDS_GENERATED) {
				logInfo(method, "adding default generated commands!");
				zclClusterInfo.getGeneratedCommands().forEach((id, name) -> {
					clusterInfo.getGeneratedCommands().put(id, new CommandInfo(id.byteValue(), name));
				});
			} else if (commandId == HaProfileCommands.DISCOVER_COMMANDS_RECEIVED) {
				logInfo(method, "adding default received commands!");
				zclClusterInfo.getReceivedCommands().forEach((id, pair) -> {
					clusterInfo.getReceivedCommands().put(id, new CommandInfo(id.byteValue(), pair.left));
				});
			} else if (commandId == HaProfileCommands.DISCOVER_ATTRIBUTES_EXTENDED) {
				logInfo(method, "adding default attributes!");
				zclClusterInfo.getAttributes().forEach((id, pair) -> {
					clusterInfo.getAttributes().put(id, new AttributeInfo(id, ZclDataType.DATA_CHARACTER_STRING,
					        pair.left, AccessType.READ_REPORTABLE));
				});
			}
		}

		// if (clusterId == 0 && (message.getData()[0] & 0x08) == 0) {
		// logInfo(method,
		// "FIRST: device: %s responses with default response on command: 0x%02x
		// (%s) of cluster: 0x%04x (%s) with status: 0x%02x (%s)",
		// address, commandId, commandName, clusterId, clusterName, statusCode,
		// status);
		// } else {
		// String commandName = CommandInfo.UNKNOWN_COMMAND;
		// ZclClusterInfo cluster = ZclClusters.getCluster(clusterId);
		// if (cluster != null) {
		// commandName = cluster.getReceivedCommands().getOrDefault(commandId,
		// DEFAULT_VALUE).getLeft();
		// }
		// logInfo(method,
		// "SECOND: device: %s responses with default response on command:
		// 0x%02x (%s) of cluster: 0x%04x (%s) "
		// + "with status: 0x%02x (%s)",
		// address, commandId, commandName, clusterId,
		// ZclClusters.getName(clusterId), statusCode, status);
		// }
	}
}
