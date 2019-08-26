package moonstone.selene.engine;

import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.text.StringSubstitutor;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.DeviceRegistrationModel;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.ErrorModel;
import moonstone.acs.client.model.ExternalHidModel;
import moonstone.selene.SeleneEventNames;
import moonstone.selene.SeleneException;
import moonstone.selene.SysUtils;
import moonstone.selene.dao.DaoManager;
import moonstone.selene.data.Device;
import moonstone.selene.data.Telemetry;
import moonstone.selene.databus.DatabusListener;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.EngineConstants.Variable;
import moonstone.selene.engine.service.ConfigService;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.GatewayService;
import moonstone.selene.engine.service.ModuleService;
import moonstone.selene.engine.service.TelemetryService;
import moonstone.selene.engine.state.DeviceStates;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateChangeHandler;
import moonstone.selene.engine.state.StateUpdate;
import moonstone.selene.model.SeleneEventModel;
import moonstone.selene.model.StatusModel;
import moonstone.selene.service.DatabusService;

public abstract class DeviceModuleAbstract<Info extends DeviceInfo, Prop extends DeviceProperties, States extends DeviceStates, Data extends DeviceData>
		extends ModuleAbstract implements DeviceModule<Info, Prop, States, Data>, DatabusListener {

	private static final Pattern COLONS = Pattern.compile(":");
	private static final StatusModel NOT_SUPPORTED_COMMAND = new StatusModel().withStatus("ERROR")
			.withMessage("Command is not supported");
	private static final StatusModel UNDEFIEND_PAYLOAD = new StatusModel().withStatus("ERROR")
			.withMessage("payload is not defined");
	protected String errorQueue = EngineConstants.DEFAULT_DEVICE_ERROR_QUEUE;
	protected List<StateChangeHandler<States>> handlers;

	private final Info info = createInfo();
	private final Prop properties = createProperties();
	private final States states = createStates();
	private final GatewayService gatewayService = GatewayService.getInstance();
	private final TelemetryService telemetryService = TelemetryService.getInstance();
	private final DeviceService deviceService = DeviceService.getInstance();

	private Thread localThread;
	private Device device;
	private long lastSentTimestamp;

	private ScriptEngine scriptEngine;
	private Map<String, String> variables;

	@Override
	public void init(Properties props) {
		String method = "DeviceModuleAbstract.init";
		Validate.notNull(props, "properties is null");

		boolean isFreshDatabase = DaoManager.getInstance().isFreshDatabase();

		try {
			if (!props.isEmpty()) {
				Map<String, String> map = new HashMap<>();
				props.stringPropertyNames().forEach(name -> {
					String value = props.getProperty(name);
					map.put(name, value);
					logDebug(method, "%s ---> %s", name, value);
				});

				// load info
				info.populateFrom(updateInfo(map));
				logDebug(method, "loaded info");

				// load properties
				properties.populateFrom(map);
				logDebug(method, "loaded properties");

				// load states
				states.importStates(Utils.convertMapToStates(map));
				logDebug(method, "loaded states");

				if (isFreshDatabase) {
					if (StringUtils.isNotEmpty(properties.getExternalPropertyFile())) {
						map.clear();
						logInfo(method, "loading additional properties file: %s", properties.getExternalPropertyFile());
						Utils.readPropertyFile(properties.getExternalPropertyFile(), map);
						info.populateFrom(updateInfo(map));
						properties.populateFrom(map);
					}

					if (StringUtils.isNotEmpty(properties.getExternalIniFile())
							&& StringUtils.isNotEmpty(properties.getExternalIniFileSection())) {
						map.clear();
						logInfo(method, "loading additional INI file: %s", properties.getExternalIniFile());
						Utils.readIniFile(properties.getExternalIniFile(), properties.getExternalIniFileSection(), map);
						Utils.readPropertyFile(properties.getExternalPropertyFile(), map);
						info.populateFrom(updateInfo(map));
						properties.populateFrom(map);
					}
				}
			}

			if (isFreshDatabase) {
				// replace variables if applicable
				getInfo().setName(replaceVariables(getInfo().getName()));
				getInfo().setUid(replaceVariables(getInfo().getUid()));
			}

			// check database if device is already registered, otherwise create
			// new device
			device = deviceService.checkAndCreateDevice(this);
			setName(String.format("%s/%s/%s", getClass().getSimpleName(), device.getName(), device.getUid()));
		} catch (Throwable t) {
			throw new SeleneException("unable to load device properties", t);
		}

		// init script engine
		String scriptFilename = getProperties().getDataParsingScriptFilename();
		if (!StringUtils.isEmpty(scriptFilename)) {
			logInfo(method, "dataParsingScriptFilename: %s", scriptFilename);
			String engineName = ConfigService.getInstance().getEngineProperties().getScriptingEngine();
			if (StringUtils.isEmpty(engineName)) {
				throw new SeleneException("script engine not defined!");
			} else {
				logInfo(method, "creating script engine: %s", engineName);
				scriptEngine = new ScriptEngineManager().getEngineByName(engineName);
			}
		}
		logInfo(method, "registering listener ...");
		DatabusService.getInstance().registerListener(this,
				EngineConstants.deviceCommandQueue(Long.toString(getDevice().getId())));
	}

	private static Map<String, String> updateInfo(Map<String, String> map) {
		Map<String, String> updatedMap = new HashMap<>();
		map.forEach((key, value) -> {
			String name = key;
			if (key.startsWith("info.")) {
				name = key.replace('.', '(') + ')';
			}
			updatedMap.put(name, value);
		});
		String value = updatedMap.remove("info");
		if (value != null) {
			for (Entry<String, String> entry : JsonUtils.fromJson(value, EngineConstants.MAP_TYPE_REF).entrySet()) {
				updatedMap.put(entry.getKey(), entry.getValue());
			}
		}
		return updatedMap;
	}

	@Override
	public void start() {
		String method = "DeviceModuleAbstract.start";
		logInfo(method, "starting local thread ...");
		localThread = new Thread(() -> {
			try {
				registerDevice();
			} catch (Exception e) {
				if (StringUtils.isEmpty(device.getHid())) {
					// throw exception if device is not previously registered
					throw e;
				} else {
					logWarn(method, "Internet is not connected ...");
					logInfo(method, "Skipping device re-registration ...");
				}
			}
			if (getProperties().isEnabled()) {
				super.start();
				if (SelfModule.getInstance().getProperties().isEnabled()) {
					startDevice();
					getDevice().setStatus(ModuleState.STARTED.name());
					persistUpdatedDeviceInfo();
					setState(ModuleState.STARTED);
				} else {
					logInfo(method, "WARNING: gateway is disabled");
					setState(ModuleState.STOPPED);
				}
			} else {
				logInfo(method, "WARNING: device is disabled");
				setState(ModuleState.STOPPED);
			}
		}, getClass().getSimpleName());
		localThread.start();
	}

	protected void startDevice() {
		String method = "DeviceModuleAbstract.startDevice";

		// sanity check
		Validate.notNull(getProperties(), "properties has not been set!");

		logInfo(method, "started");
	}

	protected void registerDevice() {
		String method = "DeviceModuleAbstract.registerDevice";
		// check if device is already registered
		Validate.notNull(device, "device is not set");

		if (StringUtils.isEmpty(device.getHid())) {
			logInfo(method, "waiting for system ready ...");
			SelfModule self = SelfModule.getInstance();
			self.waitForSystemReady();

			logInfo(method, "registerDevice ...");
			ExternalHidModel response = self.getAcnClient().getDeviceApi()
					.createOrUpdate(populate(new DeviceRegistrationModel()));
			device.setHid(response.getHid());
			device.setStatus(ModuleState.CREATED.name());
			device.setExternalId(response.getExternalId());
			device.setGatewayId(self.getGateway().getId());
			deviceService.save(device);
			logInfo(method, "persisted hid: %s, externalId: %s", device.getHid(), device.getExternalId());
		} else {
			logInfo(method, "device already registered, id: %d, hid: %s", device.getId(), device.getHid());
			// deviceService.loadDeviceProperties(this, device);
		}
	}

	@Override
	public void persistUpdatedDeviceInfo() {
		String method = "DeviceModuleAbstract.persistUpdatedDeviceInfo";

		// check if device is already registered
		Validate.notNull(device, "device is not set");

		// save local database
		logInfo(method, "persisting to database ...");
		device.setInfo(JsonUtils.toJson(getInfo().exportInfo()));
		device.setProperties(JsonUtils.toJson(getProperties().populateTo(new HashMap<>())));
		device.setStates(JsonUtils.toJson(getStates().exportStates()));
		deviceService.save(device);

		// build model to register/update
		DeviceRegistrationModel model = populate(new DeviceRegistrationModel());

		logInfo(method, "sending update to cloud ...");
		SelfModule.getInstance().getAcnClient().getDeviceApi().createOrUpdate(model);
	}

	@Override
	public void stop() {
		getDevice().setStatus(ModuleState.STOPPED.name());
		persistUpdatedDeviceInfo();
		super.stop();
		moonstone.acn.client.utils.Utils.shutdownThread(localThread);
	}

	public void queueDataForSending(DeviceData data) {
		queueDataForSending(data, false);
	}

	protected void queueDataForSending(DeviceData data, boolean processSequential) {
		String method = "DeviceModuleAbstract.queueDataForSending";

		if (getState() != ModuleState.STARTED) {
			logWarn(method, "device is not ready: %s", getState().toString());
		} else if (processSequential
				|| data.getTimestamp() - lastSentTimestamp > getProperties().getMaxPollingIntervalMs()) {

			if (scriptEngine != null) {
				if (data.getParsedIotParameters() == null) {
					data.setParsedIotParameters(new IotParameters());
				}
				if (data.getParsedTelemetries() == null) {
					data.setParsedTelemetries(new ArrayList<>());
				}
				Bindings bindings = scriptEngine.createBindings();
				bindings.put("data", data);
				bindings.put("iotParams", data.getParsedIotParameters());
				bindings.put("telemetries", data.getParsedTelemetries());
				try (FileReader reader = new FileReader(new File(getProperties().getDataParsingScriptFilename()))) {
					scriptEngine.eval(reader, bindings);
					data.setParsedFully(true);
				} catch (Exception e) {
					logError(method, e);
				}
			}

			// convert to IotParameters
			IotParameters params = data.writeIoTParameters();
			if (!params.isDirty()) {
				logInfo(method, "WARNING: no telemetries to send!");
			} else {
				if (getProperties().isPersistTelemetry()) {
					getService().submit(() -> {
						List<Telemetry> telemetries = data.writeTelemetries();
						logDebug(method, "persisting %d telemetries", telemetries.size());
						for (Telemetry telemetry : telemetries) {
							telemetry.setDeviceId(getDevice().getId());
							telemetryService.save(telemetry);
						}
					});
				}
				// DeviceHID is required
				params.setDeviceHid(getDevice().getHid());

				// additional device information
				params.setDeviceType(getInfo().getType());
				params.setExternalId(getDevice().getExternalId());

				// send to databus
				logDebug(method, "publishing message ...");
				DatabusService.getInstance().send(EngineConstants.DEFAULT_TELEMETRY_QUEUE,
						SerializationUtils.serialize(params));
				logDebug(method, "published message %d to queue", data.getTimestamp());

				// update lastSentTimestamp
				lastSentTimestamp = data.getTimestamp();
			}
		} else {
			logDebug(method, "ignored message at %d", data.getTimestamp());
		}
	}

	protected void queueStatesForSending(StateUpdate states) {
		String method = "DeviceModuleAbstract.queueStatesForSending";
		logDebug(method, "publishing states ...");
		DatabusService.getInstance().send(EngineConstants.DEFAULT_STATE_QUEUE,
				JsonUtils.toJsonBytes(states.withHid(getDevice().getHid())));
	}

	@Override
	public Info getInfo() {
		return info;
	}

	@Override
	public Prop getProperties() {
		return properties;
	}

	@Override
	public States getStates() {
		return states;
	}

	@Override
	public void receive(String queue, byte[] message) {
		String method = "DeviceModuleAbstract.receive";
		logInfo(method, "queue: %s, message size: %d", queue, message == null ? 0 : message.length);
		SeleneEventModel model = JsonUtils.fromJsonBytes(message, SeleneEventModel.class);
		StatusModel result = new StatusModel().withId(model.getId()).withName(model.getName());
		try {
			switch (model.getName()) {
			case SeleneEventNames.DEVICE_CHECK_STATUS: {
				result.withStatus(getState().name());
				break;
			}
			case SeleneEventNames.DEVICE_START: {
				if (ModuleService.getInstance().startModule(this)) {
					result.withStatus("OK").withMessage("Device started");
				} else {
					result.withStatus("ERROR").withMessage("Failed to start device");
				}
				break;
			}
			case SeleneEventNames.DEVICE_STOP: {

				if (ModuleService.getInstance().stopModule(this)) {
					result.withStatus("OK").withMessage("Device stopped");
				} else {
					result.withStatus("ERROR").withMessage("Failed to stop device");
				}
				break;
			}
			case SeleneEventNames.DEVICE_UPDATE: {
				updateDeviceFromDB();
				result.withStatus("OK");
				break;
			}

			case SeleneEventNames.DEVICE_REFRESH: {
				Device device = DeviceService.getInstance().findByTypeAndUid(getInfo().getType(), getInfo().getUid());
				Validate.notNull(device, "unknown device");
				init(Utils.getProperties(device));
				result.withStatus("OK");
				break;
			}
			case SeleneEventNames.PERFORM_COMMAND: {
				Map<String, String> payload = model.getParameters();
				result = payload == null ? UNDEFIEND_PAYLOAD : performCommand(JsonUtils.toJsonBytes(payload));
				break;
			}
			default: {
				result.withStatus("WARNING").withMessage(String.format("command: %s is unknown", model.getName()));
			}
			}
		} catch (Exception e) {
			result.withStatus("ERROR").withMessage(e.getMessage());
		}
		DatabusService.getInstance().send(model.getResponseQueue(), JsonUtils.toJsonBytes(result));
	}

	@Override
	public Device getDevice() {
		return device;
	}

	protected GatewayService getGatewayService() {
		return gatewayService;
	}

	@Override
	protected String formatLog(String method, String message) {
		String name = getInfo() == null ? "" : getInfo().getName();
		return String.format("%s|%s| %s", method, name, message);
	}

	@Override
	public Map<String, String> exportProperties() {
		return properties.populateTo(new HashMap<>());
	}

	@Override
	public void importProperties(Map<String, String> properties) {
		Validate.notNull(properties, "properties is null");
		this.properties.populateFrom(properties);
	}

	@Override
	public void notifyPropertiesChanged(Map<String, String> properties) {
		importProperties(properties);
		device.setProperties(JsonUtils.toJson(exportProperties()));
		deviceService.save(device);
	}

	@Override
	public boolean notifyStatesChanged(Map<String, State> requestStates) {
		String method = "DeviceModuleAbstract.notifyStatesChanged";

		StringBuffer currentRequestedStates = new StringBuffer();
		for (String requestState : requestStates.keySet()) {
			currentRequestedStates.append(requestState + ":" + requestStates.get(requestState).getValue());
		}

		logDebug(method, currentRequestedStates.toString());

		if (handlers != null) {
			for (StateChangeHandler<States> handler : handlers) {
				try {
					handler.handle(states, requestStates);
				} catch (Exception e) {
					logError(method, "failed to change state", e);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void notifyTelemetryChanged(Map<String, String> properties) {
	}

	protected abstract Prop createProperties();

	protected abstract Info createInfo();

	protected abstract States createStates();

	// @Override
	// protected void fillMessage(Message message) {
	// message.setObjectName(getName());
	// message.setObjectId(getInfo() == null ? "" : getInfo().getUid());
	// message.setDevice(getDevice());
	// }

	@Override
	public StatusModel performCommand(byte... bytes) {

		System.out.println("DeviceModuleAbstract.performCommand() " + bytes);

		String method = "DeviceModuleAbstract.performCommand";
		logInfo(method, "command received");
		return NOT_SUPPORTED_COMMAND;
	}

	@Override
	public void logError(String method, String message) {
		super.logError(method, message);
		DatabusService.getInstance().send(errorQueue, serialize(message));
	}

	@Override
	public void logError(String method, String message, Object... args) {
		super.logError(method, message, args);
		DatabusService.getInstance().send(errorQueue, serialize(String.format(message, args)));
	}

	@Override
	public void logError(String method, String message, Throwable throwable) {
		super.logError(method, message, throwable);
		DatabusService.getInstance().send(errorQueue,
				serialize(String.format("%s \n %s", message, ExceptionUtils.getStackTrace(throwable))));
	}

	@Override
	public void logError(String method, Throwable throwable) {
		super.logError(method, throwable);
		DatabusService.getInstance().send(errorQueue, serialize(ExceptionUtils.getStackTrace(throwable)));
	}

	@Override
	public void upgradeDeviceSoftware(File file) {
		String method = "upgradeDeviceSoftware";
		logWarn(method, "device software upgrade is not implemented");
	}

	private byte[] serialize(String message) {
		return SerializationUtils.serialize(new ImmutablePair<>(device.getHid(),
				new ErrorModel().withCode("0").withMessage(message).withTimestamp(Instant.now())));
	}

	@Override
	public DeviceRegistrationModel populate(DeviceRegistrationModel model) {
		model.setName(getInfo().getName());
		model.setType(getInfo().getType());
		model.setUid(getInfo().getUid());
		model.setGatewayHid(SelfModule.getInstance().getGateway().getHid());
		model.setInfo(getInfo().exportInfo());
		model.setProperties(exportProperties());
		return model;
	}

	@Override
	public DeviceModule<Info, Prop, States, Data> populateFromModel(DeviceRegistrationModel model) {
		getInfo().setName(model.getName());
		getInfo().setType(model.getType());
		getInfo().setUid(model.getUid());
		getInfo().importInfo(model.getInfo());
		getInfo().setInfo(model.getInfo());
		getProperties().populateFrom(model.getProperties());
		return this;
	}

	protected String replaceVariables(String value) {
		if (StringUtils.isEmpty(value)) {
			return value;
		}
		checkAndBuildVariables();
		return StringSubstitutor.replace(value, variables);
	}

	private synchronized void checkAndBuildVariables() {
		if (variables == null) {
			variables = new HashMap<>();
			variables.put(Variable.GATEWAY_UID, SelfModule.getInstance().getInfo().getUid());
			variables.put(Variable.GATEWAY_NAME, SelfModule.getInstance().getInfo().getName());
			List<String> macs = SysUtils.getMacAddresses();
			if (!macs.isEmpty()) {
				String mac = macs.get(0).trim();
				variables.put(Variable.MAC_FULL, mac);
				variables.put(Variable.MAC_SIMPLE, COLONS.matcher(mac).replaceAll("").toLowerCase(Locale.getDefault()));
			}
		}
	}

	private void updateDeviceFromDB() {
		device = deviceService.find(getInfo());
		try {
			Map<String, String> map = new HashMap<>();
			Properties props = Utils.getProperties(device);
			props.stringPropertyNames().forEach(p -> map.put(p, props.getProperty(p)));
			info.populateFrom(updateInfo(map));
			properties.populateFrom(map);
		} catch (Throwable t) {
			throw new SeleneException("Unable to load device from database", t);
		}
	}
}
