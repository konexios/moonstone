package moonstone.selene.device.conduit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import moonstone.acs.AcsSystemException;
import moonstone.acs.JsonUtils;
import moonstone.selene.SeleneException;
import moonstone.selene.dao.DeviceDao;
import moonstone.selene.data.Device;
import moonstone.selene.device.conduit.LoRaConstants.TopicType;
import moonstone.selene.device.conduit.handler.DownHandler;
import moonstone.selene.device.conduit.handler.JoinedHandler;
import moonstone.selene.device.conduit.handler.PacketRecvHandler;
import moonstone.selene.device.conduit.handler.PacketSentHandler;
import moonstone.selene.device.conduit.handler.QueueFullHandler;
import moonstone.selene.device.conduit.handler.TopicTypeHandler;
import moonstone.selene.device.conduit.handler.UpHandler;
import moonstone.selene.device.mqtt.MqttDeviceModuleAbstract;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.ModuleService;

public class LoraServerModule
        extends MqttDeviceModuleAbstract<LoraServerInfo, LoraServerProperties, LoraServerStates, LoraServerData> {

	private final static int DEFAULT_QOS = 1;

	private Map<String, TopicTypeHandler> handlers = new HashMap<>();
	private Map<String, DeviceModuleAbstract<?, ?, ?, ?>> modules = new HashMap<>();

	@Override
	public void init(Properties props) {
		super.init(props);

		String method = "init";
		String mqttTopics = getProperties().getMqttTopics();
		if (StringUtils.isEmpty(mqttTopics)) {
			throw new AcsSystemException("mqttTopics is missing in configuration");
		}
		List<String> topics = new ArrayList<>();
		for (String token : mqttTopics.split(",")) {
			topics.add(token.trim());
		}
		setSubscriptionTopics(topics);

		try {
			String deviceClassName = getProperties().getLoraDeviceClass();
			logInfo(method, "checking lora device class: %s", deviceClassName);
			Class<?> deviceClass = Class.forName(deviceClassName);
			if (!LoraDeviceModule.class.isAssignableFrom(deviceClass)) {
				throw new SeleneException("lora device class does not implement " + LoraDeviceModule.class.getName());
			}
		} catch (SeleneException e) {
			throw e;
		} catch (Exception e) {
			throw new SeleneException("invalid lora device class", e);
		}

		// init handlers
		handlers.put(TopicType.UP, new UpHandler().withServer(this));
		handlers.put(TopicType.JOINED, new JoinedHandler().withServer(this));
		handlers.put(TopicType.PACKET_RECV, new PacketRecvHandler().withServer(this));
		handlers.put(TopicType.PACKET_SENT, new PacketSentHandler().withServer(this));
		handlers.put(TopicType.QUEUE_FULL, new QueueFullHandler().withServer(this));
		handlers.put(TopicType.DOWN, new DownHandler().withServer(this));
	}

	public synchronized DeviceModuleAbstract<?, ?, ?, ?> findOrCreateModule(String uid) {
		String method = "findOrCreateModule";
		DeviceModuleAbstract<?, ?, ?, ?> result = modules.get(uid);
		if (result == null) {
			try {
				boolean started = false;
				Properties props = new Properties();
				Device existing = DeviceDao.getInstance().findByTypeAndUid(getProperties().getLoraDeviceType(), uid);
				if (existing == null) {
					logInfo(method, "creating new lora device: %s", uid);
					result = (DeviceModuleAbstract<?, ?, ?, ?>) Class.forName(getProperties().getLoraDeviceClass())
					        .getDeclaredConstructor().newInstance();
					props.setProperty("name", "Device " + uid);
					props.setProperty("uid", uid);
					props.setProperty("type", getProperties().getLoraDeviceType());
					props.setProperty("persistTelemetry", Boolean.toString(getProperties().isPersistTelemetry()));
					props.setProperty("maxPollingIntervalMs", Long.toString(getProperties().getMaxPollingIntervalMs()));
					String dataParsingScriptFilename = getProperties().getDataParsingScriptFilename();
					if (!StringUtils.isBlank(dataParsingScriptFilename)) {
						props.setProperty("dataParsingScriptFilename", dataParsingScriptFilename);
					}
				} else {
					result = (DeviceModuleAbstract<?, ?, ?, ?>) ModuleService.getInstance()
					        .findDevice(existing.getHid());
					if (result == null) {
						logWarn(method, "lora device not found for uid: %s, loading from DB ...", uid);
						result = (DeviceModuleAbstract<?, ?, ?, ?>) Class.forName(getProperties().getLoraDeviceClass())
						        .getDeclaredConstructor().newInstance();
						DeviceService.getInstance().loadDeviceProperties(result, existing);
					} else {
						started = true;
					}
				}
				if (!(result instanceof LoraDeviceModule)) {
					throw new AcsSystemException(
					        "ERROR: Device class is not of type LoraDeviceModule: " + result.getClass().getName());
				}
				((LoraDeviceModule) result).setServer(this);
				if (!started) {
					logInfo(method, "initializing lora device: %s", uid);
					result.init(props);

					logInfo(method, "registering lora service: %s", uid);
					ModuleService.getInstance().registerModule(result);

					logInfo(method, "starting lora device: %s", uid);
					ModuleService.getInstance().startModule(result);
				}
				modules.put(uid, result);
			} catch (SeleneException e) {
				throw e;
			} catch (Exception e) {
				throw new SeleneException("ERROR creating lora device", e);
			}
		}
		return result;
	}

	@Override
	public void processMessage(String topic, byte[] data) {
		String method = "processMessage";
		logInfo(method, "topic: %s", topic);
		String[] tokens = topic.split("/");
		if (tokens.length == 3) {
			String euid = tokens[1].trim();
			String type = tokens[2].trim();
			TopicTypeHandler handler = handlers.get(type);
			if (handler != null) {
				try {
					handler.handle(euid, type, data);
				} catch (Exception e) {
					logError(method, e);
				}
			} else {
				logError(method, "handler not found for type: %s", type);
			}
		} else {
			logWarn(method, "expected 3 tokens, found %d", tokens.length);
		}
	}

	public void sendDownlinkMessage(String uid, String message) {
		String method = "sendDownlinkMessage";
		String topic = String.format(LoRaConstants.DOWNLINK_TOPIC_FORMAT, uid);
		logInfo(method, "topic: %s, message: %s", topic, message);

		Map<String, String> payload = new HashMap<>();
		payload.put("data", message);
		payload.put("ack", "false");
		payload.put("port", "1");
		getMqttClient().publish(topic, JsonUtils.toJsonBytes(payload), DEFAULT_QOS);
	}

	@Override
	protected LoraServerProperties createProperties() {
		return new LoraServerProperties();
	}

	@Override
	protected LoraServerInfo createInfo() {
		return new LoraServerInfo();
	}

	@Override
	protected LoraServerStates createStates() {
		return new LoraServerStates();
	}
}
