package moonstone.selene.web.api;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import moonstone.acs.JsonUtils;
import moonstone.selene.SeleneEventNames;
import moonstone.selene.SeleneException;
import moonstone.selene.data.Device;
import moonstone.selene.databus.DatabusListenerAbstract;
import moonstone.selene.model.SeleneEventModel;
import moonstone.selene.model.StatusModel;
import moonstone.selene.service.DatabusService;
import moonstone.selene.service.DeviceService;
import moonstone.selene.web.LoginRequiredException;
import moonstone.selene.web.api.data.BleDiscoveryModel;
import moonstone.selene.web.api.data.OsModel;
import moonstone.selene.web.api.model.DeviceModels;
import moonstone.selene.web.api.model.DeviceModels.DeviceList;
import moonstone.selene.web.common.OperatingSystem;
import moonstone.selene.web.common.Utils;

@EnableScheduling
@RestController
@RequestMapping("/api/selene/devices")
public class DeviceApi extends BaseApi {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	private long gatewayId;
	private long deviceId;
	private boolean queueIsCreated = false;
	private ScriptEngineManager engineManager;
	private ScriptEngine engine;
	private Invocable invocable;
	private static boolean gatewayStatus = false;

	public DeviceApi() {
		engineManager = new ScriptEngineManager();
		engine = engineManager.getEngineByExtension("js");
		invocable = (Invocable) engine;
	}

	public long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<DeviceModels.DeviceList> all() {
		List<DeviceModels.DeviceList> devices = new ArrayList<>();

		if(getConfigService() != null) {
			for (Device device : getDeviceService().findAll()) {
				device.setGatewayId((long) 0);
				if (device.getType().equals("gateway")) {
					setGatewayId(device.getId());
				}
				devices.add(new DeviceModels.DeviceList(device));
			}

			if (!devices.isEmpty())
				Collections.sort(devices, new Comparator<DeviceModels.DeviceList>() {
					@Override
					public int compare(DeviceList o1, DeviceList o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
		}
		return devices;
	}

	@RequestMapping(value = "/{deviceId}/device", method = RequestMethod.GET)
	public DeviceModels.DeviceUpsert device(@PathVariable long deviceId) {
		Assert.notNull(deviceId, "deviceId is null");
		Device device = getDeviceService().find(deviceId);
		Assert.notNull(device, "device is null");
		setDeviceId(deviceId);
		if (!device.getType().equals("gateway")) {
			check();
		}
		return new DeviceModels.DeviceUpsert(new DeviceModels.DeviceModel(device));
	}

	@PostMapping(value = "/bleDiscovery")
	public BleDiscoveryModel bleDiscovery(@RequestBody BleDiscoveryModel model) throws Exception {
		Assert.notNull(model, "model is null");

		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR)) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running!"));
			simpMessagingTemplate.convertAndSend("/topic/device/bleDiscovery", response);
			return model;
		}

		if (Utils.getOS() != OperatingSystem.UNIX) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running Linux OS!"));
			simpMessagingTemplate.convertAndSend("/topic/device/bleDiscovery", response);
			return model;
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(SeleneEventNames.BleDiscoveryParams.INTERFACE, model.getInterfaceName());
		parameters.put(SeleneEventNames.BleDiscoveryParams.DISCOVERY_TIMEOUT,
				String.valueOf(model.getDiscoveryTimeout() * 1000));
		String command = JsonUtils
				.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString()).withParameters(parameters)
						.withName(SeleneEventNames.BLE_DISCOVERY).withResponseQueue("selene-portal"));
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(0l, command)) {
			logInfo("bleDiscovery", "sending a new command: " + command);
			getResponseFromDevice();
		}
		return model;
	}

	@RequestMapping(value = "/{deviceId}/start", method = RequestMethod.GET)
	public void start(@PathVariable long deviceId) {
		Assert.notNull(deviceId, "deviceId is null");
		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR)) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running!"));
			simpMessagingTemplate.convertAndSend("/topic/device/" + deviceId, response);
			return;
		}

		String command = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
				.withName(SeleneEventNames.DEVICE_START).withResponseQueue("selene-portal"));
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(deviceId, command)) {
			logInfo("start", "sending a new command: " + command);
			getResponseFromDevice();
		}
	}

	@RequestMapping(value = "/{deviceId}/stop", method = RequestMethod.GET)
	public void stop(@PathVariable long deviceId) {
		Assert.notNull(deviceId, "deviceId is null");

		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR)) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running!"));
			simpMessagingTemplate.convertAndSend("/topic/device/" + deviceId, response);
			return;
		}

		String command;
		if (deviceId == 0) {
			// For gateway
			setDeviceId(0);
			command = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
					.withName(SeleneEventNames.GATEWAY_STOP).withResponseQueue("selene-portal"));
		} else {
			// For devices
			command = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
					.withName(SeleneEventNames.DEVICE_STOP).withResponseQueue("selene-portal"));
		}
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(deviceId, command)) {
			logInfo("stop", "sending a new command: " + command);
			getResponseFromDevice();
		}
	}

	@RequestMapping(value = "/{deviceId}/performCommand", method = RequestMethod.POST)
	public void performCommand(@PathVariable long deviceId, @RequestBody String data) {
		Assert.notNull(deviceId, "deviceId is null");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("payload", data);
		parameters.put("command", "set_lookup_configuration");
		String command = JsonUtils.toJson(
				new SeleneEventModel().withId(UUID.randomUUID().toString()).withName(SeleneEventNames.PERFORM_COMMAND)
				.withParameters(parameters).withResponseQueue("selene-portal"));
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(deviceId, command)) {
			logInfo("performCommand", "sending a new command: " + command);
			getResponseFromDevice();
		}
	}

	@RequestMapping(value = "/{deviceId}/checkGatewayStatus", method = RequestMethod.GET)
	public void checkGatewayStatus(@PathVariable long deviceId) {
		Assert.notNull(deviceId, "deviceId is null");
		String command = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
				.withName(SeleneEventNames.GATEWAY_CHECK_STATUS).withResponseQueue("selene-portal"));
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(deviceId, command)) {
			logInfo("checkGatewayStatus", "sending a new command: " + command);
			getResponseFromDevice();
		}
	}

	@RequestMapping(value = "/{deviceId}/checkDeviceStatus", method = RequestMethod.GET)
	@Scheduled(fixedDelay = 1800000)
	private void check() {
		String json = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
				.withName(SeleneEventNames.DEVICE_CHECK_STATUS).withResponseQueue("selene-portal"));
		Assert.notNull(json, "command is null");
		try {	
			//If gateway is started  gracefully and Config-service is created then only send device-commands.
			if(getConfigService() != null) {
				getDeviceService().sendCommand(getDeviceId(), json);
				logInfo("checkDeviceStatus", "sending a new command: " + json);
				getResponseFromDevice();
			}
		} catch (LoginRequiredException e) {
		}
	}

	@RequestMapping(value = "/createDevice", method = RequestMethod.POST)
	public void createDevice(@RequestBody Device device) {
		device.setGatewayId(getGatewayId());
		device.setStatus("CREATED");
		device.setStates("{}");

		String method = "createDevice";

		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR)) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running!"));
			simpMessagingTemplate.convertAndSend("/topic/device/create", response);
			return;
		}

		logInfo(method, "Device Name :: %s", device.getName());
		logInfo(method, "Device Info :: %s", device.getInfo());
		logInfo(method, "Device Properties :: %s", device.getProperties());

		if (DeviceService.getInstance().createDevice(device)) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put(SeleneEventNames.DeviceLoadParams.DEVICE_TYPE, device.getType());
			parameters.put(SeleneEventNames.DeviceLoadParams.DEVICE_UID, device.getUid());
			String command = JsonUtils.toJson(
					new SeleneEventModel().withId(UUID.randomUUID().toString()).withName(SeleneEventNames.DEVICE_LOAD)
					.withParameters(parameters).withResponseQueue("selene-portal"));
			Assert.notNull(command, "command is null");
			DeviceService.getInstance().sendCommand(getGatewayId(), command);
			logInfo(method, "sending a new command: " + command);
			logInfo(method, "device " + device.getType() + ", " + device.getUid() + " added to DB");
		}
	}

	@RequestMapping(value = "/{deviceId}/updateDevice", method = RequestMethod.PUT)
	public Device updateDevice(@PathVariable long deviceId, @RequestBody Device device) throws Exception {
		String method = "updateDevice";

		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR))
			throw new Exception("Gateway is not running!");

		if (deviceId == 0)
			throw new Exception("Gateway device can not be updated!");

		logInfo(method, "Device Name :: %s", device.getName());
		logInfo(method, "Device Info :: %s", device.getInfo());
		logInfo(method, "Device Properties :: %s", device.getProperties());

		Device device1 = getDeviceService().find(deviceId);
		if (device1 != null) {
			device.setHid(device1.getHid());
			device.setStatus(device1.getStatus());
			device.setStates(device1.getStates());
			DeviceService.getInstance().save(device);

			String command = JsonUtils.toJson(new SeleneEventModel().withId(UUID.randomUUID().toString())
					.withName(SeleneEventNames.DEVICE_UPDATE).withResponseQueue("selene-portal"));
			Assert.notNull(command, "command is null");
			DeviceService.getInstance().sendCommand(deviceId, command);

			logInfo(method, "Device with deviceId " + deviceId + " updated successfully...!");
		} else
			logInfo(method, "Device with deviceId " + deviceId + " not found");

		return device;
	}

	@RequestMapping(value = "/{deviceId}/deleteDevice", method = RequestMethod.DELETE)
	public void deleteDevice(@PathVariable long deviceId, @RequestBody String deviceUID) {
		Assert.notNull(deviceId, "deviceId is null");

		if (!gatewayStatus && !Utils.isProcessRunning(GatewayApi.SELENE_ENGINE_JAR)) {
			String response = JsonUtils
					.toJson(new StatusModel().withStatus("ERROR").withMessage("Gateway is not running!"));
			simpMessagingTemplate.convertAndSend("/topic/device/" + getDeviceId(), response);
			return;
		}

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(SeleneEventNames.DeviceLoadParams.DEVICE_UID, deviceUID);
		String command = JsonUtils.toJson(
				new SeleneEventModel().withId(UUID.randomUUID().toString()).withName(SeleneEventNames.DEVICE_UNLOAD)
				.withParameters(parameters).withResponseQueue("selene-portal"));
		Assert.notNull(command, "command is null");
		if (getDeviceService().sendCommand(getGatewayId(), command)) {
			logInfo("deleteDevice", "sending a new command: " + command);
			getResponseFromDevice();
		}
	}

	private void getResponseFromDevice() {
		if (!queueIsCreated) {
			DatabusService.getInstance().registerListener(new DeviceListener(getClass().getName()), "selene-portal");
			queueIsCreated = true;
		}
	}

	public class DeviceListener extends DatabusListenerAbstract {
		DeviceListener(String name) {
			super(name);
		}

		@Override
		public void receive(String queue, byte[] message) {
			String method = "receive";
			try {
				String msg = new String(message);
				moonstone.selene.model.StatusModel model = JsonUtils.fromJsonBytes(message,
						moonstone.selene.model.StatusModel.class);

				logInfo(method, "getting a response from the device: " + msg);

				if (Objects.equals(model.getName(), SeleneEventNames.BLE_DISCOVERY)) {
					simpMessagingTemplate.convertAndSend("/topic/device/bleDiscovery", msg);
					return;
				}
				String msgStr = msg.substring(0, msg.length() - 1).concat(",\"deviceId\":\"" + getDeviceId() + "\"}");

				if (Objects.equals(model.getName(), SeleneEventNames.DEVICE_LOAD)) {
					simpMessagingTemplate.convertAndSend("/topic/device/create", msgStr);
					return;
				}

				if (Objects.equals(model.getName(), SeleneEventNames.GATEWAY_CHECK_STATUS)) {
					gatewayStatus = Objects.equals(model.getStatus(), "OK");
				}

				simpMessagingTemplate.convertAndSend("/topic/device/" + getDeviceId(), msgStr);

			} catch (Exception e) {
				logError(method, "error in received data: %s", e);
			}
		}
	}

	public Device findByUid(String uid) {
		for (Device device : getDeviceService().findAll()) {
			if (device.getUid().equals(uid)) {
				return device;
			}
		}
		return null;
	}

	@RequestMapping(value = "/{deviceId}/saveTranspose/{transposeType}", method = RequestMethod.POST)
	public void saveTranspose(@PathVariable long deviceId, @PathVariable String transposeType,
			@RequestBody String data) {
		String method = "saveTranspose";
		Assert.notNull(deviceId, "deviceId is null");
		Assert.notNull(transposeType, "transposeType is null");
		Assert.notNull(data, "data is null");

		try {
			Device device = getDeviceService().find(deviceId);
			Assert.notNull(device, "No device found");
			JsonNode payload = JsonUtils.fromJson(data, JsonNode.class);
			String fileName = device.getUid() + "_" + transposeType + ".js";
			String filePath = getConfigService().getWebProperties().getTransponsePath();
			if (StringUtils.isEmpty(filePath)) {
				filePath = OsModel.getDeviceConfigPath() + fileName;
			} else {
				filePath += fileName;
			}
			File file = new File(filePath);
			JsonNode properties = JsonUtils.fromJson(device.getProperties(), JsonNode.class);
			try (FileWriter writer = new FileWriter(file)) {
				logInfo(method, "file created : " + filePath);
				writer.write(payload.get("transposeFunction").asText());
			} catch (IOException e) {
				logError(method, e);
			}
			if (transposeType.equals("registration")) {
				((ObjectNode) properties).put("registrationTransposerScript", filePath);
			} else if (transposeType.equals("telemetry")) {
				((ObjectNode) properties).put("telemetryTransposerScript", filePath);
			} else if (transposeType.equals("state")) {
				((ObjectNode) properties).put("stateTransposerScript", filePath);
			}
			device.setProperties(JsonUtils.toJson(properties));
			getDeviceService().save(device);
		} catch (Exception e) {
			throw new SeleneException("ERROR in saving transpose file", e);

		}
	}

	@RequestMapping(value = "/{deviceId}/getTranspose/{transposeType}", method = RequestMethod.GET)
	public StatusModel getTranspose(@PathVariable long deviceId, @PathVariable String transposeType) {
		Assert.notNull(deviceId, "deviceId is null");
		Assert.notNull(transposeType, "transposeType is null");
		try {
			Device device = getDeviceService().find(deviceId);
			Assert.notNull(device, "No device found");
			String fileName = getConfigService().getWebProperties().getTransponsePath();
			if (StringUtils.isEmpty(fileName)) {
				fileName = OsModel.getDeviceConfigPath() + device.getUid() + "_" + transposeType + ".js";
			} else {
				fileName += device.getUid() + "_" + transposeType + ".js";
			}
			logInfo("getTranspose", "reading %s file", fileName);
			String transposeFunction = new String(Files.readAllBytes(Paths.get(fileName)));
			return new StatusModel().withStatus("OK").withMessage(transposeFunction);
		} catch (Exception e) {
			return new StatusModel().withStatus("ERROR").withMessage(e.getMessage());
		}
	}

	@RequestMapping(value = "/{deviceId}/deleteTranspose/{transposeType}", method = RequestMethod.DELETE)
	public void deleteTranspose(@PathVariable long deviceId, @PathVariable String transposeType) {
		String method = "deleteTranspose";
		Assert.notNull(deviceId, "deviceId is null");
		Assert.notNull(transposeType, "transposeType is null");
		try {
			Device device = getDeviceService().find(deviceId);
			Assert.notNull(device, "No device found");
			String fileName = device.getUid() + "_" + transposeType + ".js";
			String filePath = getConfigService().getWebProperties().getTransponsePath();
			if (StringUtils.isEmpty(filePath)) {
				filePath = OsModel.getDeviceConfigPath() + fileName;
			} else {
				filePath += fileName;
			}
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
				JsonNode properties = JsonUtils.fromJson(device.getProperties(), JsonNode.class);
				if (transposeType.equals("registration")) {
					((ObjectNode) properties).remove("registrationTransposerScript");
				} else if (transposeType.equals("telemetry")) {
					((ObjectNode) properties).remove("telemetryTransposerScript");
				} else if (transposeType.equals("state")) {
					((ObjectNode) properties).remove("stateTransposerScript");
				}
				device.setProperties(JsonUtils.toJson(properties));
				getDeviceService().save(device);
			} else {
				throw new SeleneException("ERROR in deleting transpose file");
			}
			logInfo(method, filePath + " file deleted...");
		} catch (Exception e) {
			throw new SeleneException("ERROR in deleting transpose file", e);
		}
	}

	@RequestMapping(value = "/testRegTelTranspose", method = RequestMethod.POST)
	public String testRegTelTranspose(@RequestBody String data) throws JSONException, ScriptException {

		Assert.notNull(data, "data is null");
		try {
			JSONObject payload = new JSONObject(data);
			String function = payload.getString("transposeFunction");
			String incomingPayload = payload.getString("incomingPayload");

			engine.eval(function);
			String result = (String) invocable.invokeFunction("transpose", incomingPayload);
			return result;
		} catch (NoSuchMethodException e) {
			throw new SeleneException("ERROR in testing transpose function", e);
		}
	}

	@RequestMapping(value = "/testStateTranspose", method = RequestMethod.POST)
	public String testStateTranspose(@RequestBody String data) throws JSONException, ScriptException {
		Assert.notNull(data, "data is null");
		try {
			JSONObject payload = new JSONObject(data);
			String function = payload.getString("transposeFunction");
			String deviceUid = payload.getString("deviceUid");
			String deviceName = payload.getString("deviceName");
			String deviceStates = payload.getString("deviceStates");

			engine.eval(function);
			String result = (String) invocable.invokeFunction("transpose", deviceUid, deviceName, deviceStates);

			return result;
		} catch (NoSuchMethodException e) {
			throw new SeleneException("ERROR in testing state transpose function", e);
		}
	}
}