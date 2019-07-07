package com.arrow.selene.device.zigbee;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.arrow.acs.JsonUtils;
import com.arrow.selene.device.xbee.zcl.ApplicationProfiles;
import com.arrow.selene.device.xbee.zdo.data.Neighbor;
import com.arrow.selene.device.zigbee.handlers.commands.CommandHandler;
import com.arrow.selene.engine.DeviceData;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.model.StatusModel;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.fasterxml.jackson.core.type.TypeReference;

public abstract class ZigBeeModuleAbstract<Info extends ZigBeeInfoAbstract, Props extends ZigBeePropertiesAbstract, States extends ZigBeeStatesAbstract, Data extends ZigBeeData>
        extends DeviceModuleAbstract<Info, Props, States, Data> {
	private static AtomicInteger SEQUENCE = new AtomicInteger(0);
	private static TypeReference<Map<String, String>> mapTypeRef;

	protected Map<String, CommandHandler> commandHandlers = new HashMap<>();
	protected Map<String, Neighbor> neighbors = new HashMap<>();
	protected ZigBeeCoordinatorModule hub;

	protected Map<Integer, MessageInfo> messages = new ConcurrentHashMap<>();

	@Override
	public StatusModel performCommand(byte... bytes) {
		String method = "ZigBeeModuleAbstract.performCommand";
		super.performCommand(bytes);
		Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
		String command = params.get(CommandHandler.COMMAND_PROPERTY_NAME);
		logInfo(method, "received command: %s", command);
		StatusModel result = StatusModel.OK;
		CommandHandler handler = commandHandlers.get(command);
		if (handler == null) {
			logWarn(method, "unknown command: %s", command);
			result.withStatus("WARNING").withMessage(String.format("unknown command: %s", command));
		} else {
			logDebug(method, "found handler: %s", handler.getName());
			handler.handle(command, params.get(CommandHandler.PAYLOAD_PROPERTY_NAME));
		}
		return result;
	}

	public Map<String, Neighbor> getNeighbors() {
		return neighbors;
	}

	public void publishTelemetry(DeviceData data) {
		getService().submit(() -> queueDataForSending(data));
	}

	public void publishHealthData() {
		getService().submit(() -> queueDataForSending(new ZigBeeHealthData(neighbors)));
	}

	public void sendZdoMessage(XBee64BitAddress address, int clusterId, byte[] payload) {
		hub.sendZdoMessage(address, clusterId, payload);
	}

	public void sendMessage(XBee64BitAddress address, int dstEndpoint, int clusterId, int profileId, byte[] payload) {
		hub.sendMessage(address, dstEndpoint, clusterId, profileId, payload);
	}

	public synchronized void addMessage(MessageInfo message) {
		String method = "addMessage";
		logInfo(method, "message: %s", message.toString());
		if (messages.containsKey(message.getSequence())) {
			logWarn(method, "WARNING: message exists at key: %d", message.getSequence());
		}
		messages.put(Byte.toUnsignedInt(message.getSequence()), message);
		sendMessage(message);
	}

	public void sendMessage(MessageInfo message) {
		message.decrementRetries();
		String method = "sendMessage";
		logInfo(method, "message: %s", message.toString());

		message.tryNow();
		switch (message.getType()) {
		case ZDO_MESSAGE: {
			sendZdoMessage(new XBee64BitAddress(message.getAddress()), message.getClusterId(), message.getPayload());
			break;
		}
		case HA_PROFILE_MESSAGE: {
			sendMessage(new XBee64BitAddress(message.getAddress()), message.getEndpoint(), message.getClusterId(),
			        ApplicationProfiles.HOME_AUTOMATION_PROFILE, message.getPayload());
			break;
		}
		case ZLL_PROFILE_MESSAGE: {
			logInfo(method, "sending ZLL_PROFILE_MESSAGE clusterId: %s", message.getClusterId());
			sendMessage(new XBee64BitAddress(message.getAddress()), message.getEndpoint(), message.getClusterId(),
			        ApplicationProfiles.LIGHT_LINK_PROFILE, message.getPayload());
			break;
		}
		default: {
			logError(method, "resending messages of type: %s is not implemented", message.getType());
			break;
		}
		}
	}

	public synchronized MessageInfo removeMessage(byte sequence) {
		String method = "removeMessage";
		int key = Byte.toUnsignedInt(sequence);
		MessageInfo result = messages.remove(key);
		if (result != null) {
			logInfo(method, "message removed successfully: %s", key, result.toString());
		} else {
			logWarn(method, "message not found for sequence: %d", key);
		}

		logInfo(method, "messages size: %d", messages.size());
		if (isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			messages.values().forEach(msg -> {
				sb.append(msg.getDescription());
				sb.append(", ");
			});
			logDebug(method, "----> " + sb.toString());
		}
		if (messages.isEmpty()) {
			persistUpdatedDeviceInfo();
		}
		return result;
	}

	public Map<Integer, MessageInfo> getMessages() {
		return messages;
	}

	public byte nextSequence() {
		return (byte) (SEQUENCE.incrementAndGet() % 256);
	}

	private static TypeReference<Map<String, String>> getMapTypeRef() {
		return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
		});
	}
}
