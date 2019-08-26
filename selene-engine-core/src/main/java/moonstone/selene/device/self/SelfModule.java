package moonstone.selene.device.self;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.api.AcnClient;
import moonstone.acn.client.api.GatewayApi;
import moonstone.acn.client.model.AwsConfigModel;
import moonstone.acn.client.model.AzureConfigModel;
import moonstone.acn.client.model.CreateGatewayModel;
import moonstone.acn.client.model.GatewayConfigModel;
import moonstone.acn.client.model.IbmConfigModel;
import moonstone.acs.AcsUtils;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.api.ApiConfig;
import moonstone.acs.client.model.ExternalHidModel;
import moonstone.acs.client.model.KeyModel;
import moonstone.acs.client.model.VersionModel;
import moonstone.selene.SeleneCoreVersion;
import moonstone.selene.SeleneEventNames;
import moonstone.selene.SeleneException;
import moonstone.selene.SysUtils;
import moonstone.selene.SeleneEventNames.DeviceLoadParams;
import moonstone.selene.dao.DaoManager;
import moonstone.selene.data.Device;
import moonstone.selene.data.Gateway;
import moonstone.selene.engine.DeviceModule;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.DirectoryManager;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.EngineProperties;
import moonstone.selene.engine.ModuleState;
import moonstone.selene.engine.SeleneEngineCoreVersion;
import moonstone.selene.engine.ShutdownListener;
import moonstone.selene.engine.Utils;
import moonstone.selene.engine.service.AwsService;
import moonstone.selene.engine.service.AzureService;
import moonstone.selene.engine.service.ConfigService;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.GatewayService;
import moonstone.selene.engine.service.IbmService;
import moonstone.selene.engine.service.ModuleService;
import moonstone.selene.engine.service.SshTunnelService;
import moonstone.selene.model.ProcessStatusModel;
import moonstone.selene.model.SeleneEventModel;
import moonstone.selene.model.StatusModel;
import moonstone.selene.service.CryptoService;
import moonstone.selene.service.DatabusService;

public class SelfModule extends DeviceModuleAbstract<SelfInfo, SelfProperties, SelfStates, SelfData> {

	private static final long MILLISECONDS_IN_DAY = 24L * 60L * 60L * 1000L;
	private static final TypeReference<Map<String, String>> MAP_TYPE_REF = new TypeReference<Map<String, String>>() {
	};

	private static TypeReference<Map<String, String>> mapTypeRef;

	private static class SingletonHolder {
		static final SelfModule SINGLETON = new SelfModule();
	}

	public static SelfModule getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private Gateway gateway;
	private volatile boolean systemReady;

	private Thread registrationThread;
	private List<Timer> timers = new ArrayList<>(3);

	private AcnClient acnClient;
	private List<ShutdownListener> shutdownListeners = new ArrayList<>();

	private SelfModule() {
		errorQueue = EngineConstants.DEFAULT_GATEWAY_ERROR_QUEUE;
	}

	@Override
	public void init(Properties props) {
		String method = "init";

		getInfo().setNetworks(detectNetworks());
		getInfo().setVersions(new VersionModel[] { SeleneCoreVersion.get(), SeleneEngineCoreVersion.get() });

		// default UID to first mac address, UID in properties file will
		// override this
		List<String> addresses = SysUtils.getMacAddresses();
		if (addresses.isEmpty()) {
			logWarn(method, "Unable to determine MAC address");
		} else {
			String uid = String.format("%s-%s", SelfInfo.DEFAULT_DEVICE_TYPE.toLowerCase(),
					addresses.get(0).replaceAll(":", ""));
			logInfo(method, "set default gateway UID to %s", uid);
			getInfo().setUid(uid);
		}

		super.init(props);

		// Force disable sending gateway and device errors until
		// corresponding APIs are implemented
		getProperties().setSendDeviceErrors(false);
		getProperties().setSendGatewayErrors(false);

		// check for override logInfo
		String logLevel = getInfo().getLogLevel();
		if (StringUtils.isNotEmpty(logLevel)) {
			try {
				Level level = Level.toLevel(logLevel, null);
				if (level != null) {
					LoggerContext context = (LoggerContext) LogManager.getContext(false);
					Configuration configuration = context.getConfiguration();
					for (LoggerConfig config : configuration.getLoggers().values()) {
						// only update selene logging
						if (config.getName().contains("selene")) {
							config.setLevel(level);
							logInfo(method, "updated logLevel to: %s for: %s", level.toString(), config.getName());
						}
					}
					context.updateLoggers();
				} else {
					logError(method, "Invalid logLevel found: %s", logLevel);
				}
			} catch (Exception ignored) {
				logError(method, "Error overriding logLevel: %s", logLevel);
			}
		}

		// init acnClient
		CryptoService crypto = CryptoService.getInstance();
		crypto.init(getProperties().getCryptoMode());
		ApiConfig config = new ApiConfig().withApiKey(crypto.decrypt(getProperties().getApiKey()))
				.withSecretkey(crypto.decrypt(getProperties().getSecretKey()))
				.withBaseUrl(getProperties().getIotConnectUrl());

		logInfo(method, "initializing acnClient ...");
		acnClient = new AcnClient(config);
	}

	public void addShutdownListener(ShutdownListener listener) {
		if (listener != null) {
			shutdownListeners.add(listener);
		}
	}

	public void shutdown() {
		shutdownListeners.forEach(listener -> listener.shutdown());
	}

	private NetworkIface[] detectNetworks() {
		List<NetworkIface> result = new ArrayList<>();
		try {
			for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (!networkInterface.isVirtual() && !networkInterface.isPointToPoint()
						&& !networkInterface.isLoopback()) {
					result.add(getInterfaceInfo(networkInterface));
				}
			}
		} catch (Exception ignored) {
		}
		return result.toArray(new NetworkIface[result.size()]);
	}

	@Override
	protected void registerDevice() {
		String method = "registerDevice";
		try {
			registrationThread = new Thread(new RegistrationTask(), "registrationThread");
			registrationThread.start();
			registrationThread.join();
			super.registerDevice();
		} catch (Exception e) {
			logError(method, "error starting selfModule", e);
		}
	}

	@Override
	protected void startDevice() {
		super.startDevice();
		persistUpdatedDeviceInfo();
	}

	@Override
	public void stop() {
		String method = "stop";
		moonstone.acn.client.utils.Utils.shutdownThread(registrationThread);
		logInfo(method, "cancelling timers ...");
		timers.forEach(Timer::cancel);
	}

	public AcnClient getAcnClient() {
		Validate.notNull(acnClient, "acnClient is not initialized");
		return acnClient;
	}

	@Override
	public StatusModel performCommand(byte... bytes) {

		System.out.println("SelfModule.performCommand() " + bytes);

		String method = "performCommand";
		super.performCommand(bytes);
		Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
		String command = params.get("command");
		logInfo(method, "command: %s", command);
		StatusModel result = StatusModel.OK;
		if (Objects.equals(command, "restartGateway")) {
			logInfo(method, "restarting gateway");
			EngineProperties properties = ConfigService.getInstance().getEngineProperties();
			String restartScriptFilename = properties.getRestartScriptFilename();
			Validate.notBlank(restartScriptFilename, "restart script filename is not defined");
			File script = new File(new File(DirectoryManager.getInstance().getConfig(), "scripts"),
					restartScriptFilename);
			if (script.exists()) {
				shutdown();
				try {
					String scriptPath = script.getAbsolutePath();
					logInfo(method, "executing %s script", scriptPath);
					Runtime.getRuntime().exec(scriptPath);
				} catch (IOException e) {
					logError(method, e);
					result.withStatus("ERROR").withMessage(e.getMessage());
				}
			} else {
				logWarn(method, "restart script does not exist");
				result.withStatus("WARNING").withMessage("restart script does not exist");
			}
		} else if (Objects.equals(command, "openSshTunnel")) {
			ensureLinuxEnvironment();
			try {
				SshTunnelService.getInstance().openTunnel(params);
			} catch (Throwable t) {
				logError(method, "ERROR opening tunnel", t);
				result.withStatus("ERROR")
						.withMessage(String.format("ERROR opening tunnel \n %s", AcsUtils.getStackTrace(t)));
			}
		} else if (Objects.equals(command, "closeSshTunnel")) {
			ensureLinuxEnvironment();
			try {
				SshTunnelService.getInstance().closeTunnel(params);
			} catch (Throwable t) {
				logError(method, "ERROR closing tunnel", t);
				result.withStatus("ERROR")
						.withMessage(String.format("ERROR closing tunnel \n %s", AcsUtils.getStackTrace(t)));
			}
		}
		return result;
	}

	public synchronized void waitForSystemReady() {
		String method = "waitForSystemReady";
		if (!systemReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				logError(method, e);
			}
		}
	}

	NetworkIface getInterfaceInfo(NetworkInterface netint) throws SocketException {
		NetworkIface result = new NetworkIface();
		result.setDisplayName(netint.getDisplayName());
		result.setName(netint.getName());

		byte[] hardwareAddress = netint.getHardwareAddress();
		if (hardwareAddress != null) {
			result.setHardwareAddress(String.join(":", Arrays.stream(ArrayUtils.toObject(hardwareAddress))
					.map(Utils::byteToHexString).collect(Collectors.toList())));
		}
		result.setInetAddresses(Collections.list(netint.getInetAddresses()).stream().map(InetAddress::getHostAddress)
				.collect(Collectors.toList()));
		return result;
	}

	@Override
	protected SelfProperties createProperties() {
		return new SelfProperties();
	}

	@Override
	protected SelfInfo createInfo() {
		return new SelfInfo();
	}

	@Override
	protected SelfStates createStates() {
		return new SelfStates();
	}

	public Gateway getGateway() {
		return gateway;
	}

	private synchronized void notifySystemReady() {
		String method = "notifySystemReady";

		logInfo(method, "...");
		systemReady = true;
		registrationThread = null;
		notifyAll();

		// only start heartbeat timer when system is ready
		if (systemReady && !isShuttingDown()) {
			startHeartbeatTimer();
			startHealthCheckTimer();
			startTelemetryPurgeTimer();
			startMessagesPurgeTimer();
		}
	}

	private void startHeartbeatTimer() {
		String method = "startHeartbeatTimer";
		Validate.notNull(gateway, "gateway is null");
		if (gateway.getHeartBeatIntervalMs() > 0) {
			Timer timer = new Timer("Heartbeat", true);
			timer.schedule(new HeartbeatTimerTask(gateway), 0L, gateway.getHeartBeatIntervalMs());
			timers.add(timer);
			logInfo(method, "heartbeat timer started");
		} else {
			logWarn(method, "heartbeat timer is disabled in configuration");
		}
	}

	private void startHealthCheckTimer() {
		String method = "startHealthCheckTimer";
		Validate.notNull(gateway, "gateway is null");
		int healthCheckIntervalMins = getProperties().getHealthCheckIntervalMins();
		if (healthCheckIntervalMins > 0) {
			Timer timer = new Timer("HealthCheck", true);
			timer.schedule(new HealthCheckTimerTask(), 0L, healthCheckIntervalMins * 60L * 1000L);
			timers.add(timer);
			logInfo(method, "health check timer started");
		} else {
			logWarn(method, "health check timer is disabled in configuration");
		}
	}

	private void startTelemetryPurgeTimer() {
		String method = "startTelemetryPurgeTimer";
		Validate.notNull(gateway, "gateway is null");
		if (gateway.getPurgeTelemetryIntervalDays() > 0) {
			Timer timer = new Timer("TelemetryPurge", true);
			timer.schedule(new PurgeTelemetryTimerTask(gateway), 0L, MILLISECONDS_IN_DAY);
			timers.add(timer);
			logInfo(method, "telemetry purge timer started");
		} else {
			logWarn(method, "telemetry purge timer is disabled in configuration");
		}
	}

	private void startMessagesPurgeTimer() {
		String method = "startMessagesPurgeTimer";
		Validate.notNull(gateway, "gateway is null");
		if (gateway.getPurgeMessagesIntervalDays() > 0) {
			Timer timer = new Timer("MessagePurge", true);
			timer.schedule(new PurgeMessagesTimerTask(gateway), 0L, MILLISECONDS_IN_DAY);
			timers.add(timer);
			logInfo(method, "messages purge timer started");
		} else {
			logWarn(method, "messages purge timer is disabled in configuration");
		}
	}

	private static TypeReference<Map<String, String>> getMapTypeRef() {
		return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
		});
	}

	private class RegistrationTask implements Runnable {
		@Override
		public void run() {
			String method = "RegistrationTask.run";
			GatewayApi gatewayApi = getAcnClient().getGatewayApi();

			while (true) {
				try {
					// first look up in database
					logInfo(method, "checkAndCreateDefaultGateway ...");
					gateway = GatewayService.getInstance().checkAndCreateDefaultGateway(SelfModule.this);

					// TODO: TEMPORARY FIX - call register again to update
					// version
					try {
						logInfo(method, "registerGateway ...");

						Validate.notNull(gateway, "gateway is null");
						ExternalHidModel response = gatewayApi.registerNewGateway(
								GatewayService.getInstance().populateModel(gateway, new CreateGatewayModel()));
						gateway.setHid(response.getHid());
						gateway.setExternalId(response.getExternalId());
						logInfo(method, "persisted hid: %s, externalId: %s", gateway.getHid(), gateway.getExternalId());

						logInfo(method, "checkinGateway ...");
						gatewayApi.checkin(gateway.getHid());

						GatewayConfigModel config = gatewayApi.downloadGatewayConfiguration(gateway.getHid());
						gateway.setCloudPlatform(config.getCloudPlatform());
						KeyModel keyModel = config.getKey();
						if (keyModel != null) {
							gateway.setApiKey(getCryptoService().encrypt(keyModel.getApiKey()));
							gateway.setSecretKey(getCryptoService().encrypt(keyModel.getSecretKey()));

							// update KronosClient with new keys
							getAcnClient().getApiConfig().withApiKey(keyModel.getApiKey())
									.withSecretkey(keyModel.getSecretKey());
						}
						GatewayService.getInstance().update(gateway);
						logInfo(method, "updated gateway, cloudPlatform: %s", gateway.getCloudPlatform());

						AwsConfigModel awsModel = config.getAws();
						if (awsModel != null) {
							AwsService.getInstance().upsert(awsModel);
							logInfo(method, "updated AWS information");
						} else {
							AwsService.getInstance().checkAndDisable();
						}

						IbmConfigModel ibmModel = config.getIbm();
						if (ibmModel != null) {
							IbmService.getInstance().upsert(ibmModel);
							logInfo(method, "updated IBM information");
						} else {
							IbmService.getInstance().checkAndDisable();
						}

						AzureConfigModel azureModel = config.getAzure();
						if (azureModel != null) {
							AzureService.getInstance().upsert(azureModel);
							logInfo(method, "updated Azure information");
						} else {
							AzureService.getInstance().checkAndDisable();
						}
					} catch (Throwable e) {
						if (isShuttingDown()) {
							logInfo(method, "force terminating ...");
							break;
						} else if (!StringUtils.isEmpty(gateway.getHid())) {
							logWarn(method, "Internet connectivity failure ...");
							logInfo(method, "working in offline mode ...");
							new Thread(new CheckInTask()).start();
						} else {
							// Continue retry of gateway registration
							continue;
						}
					}

					notifySystemReady();

					Set<Properties> props = new HashSet<>();
					if (DaoManager.getInstance().isFreshDatabase()) {
						props = loadPropsFromFiles(DirectoryManager.getInstance().getDevices().getPath());
					} else {
						props = loadPropsFromDB();
					}
					initDevices(props);
					break;
				} catch (Throwable e) {
					if (isShuttingDown()) {
						logInfo(method, "force terminating ...");
						break;
					} else {
						logError(method, "Registration Task FAILED!", e);
						try {
							logInfo(method, "Retrying in %d ms", EngineConstants.DEFAULT_GATEWAY_REGISTRATION_RETRY_MS);
							Thread.sleep(EngineConstants.DEFAULT_GATEWAY_REGISTRATION_RETRY_MS);
						} catch (Exception ignored) {
						}
					}
				}
			}
		}
	}

	private class CheckInTask implements Runnable {

		@Override
		public void run() {
			String method = "CheckInTask.run";
			GatewayApi gatewayApi = getAcnClient().getGatewayApi();

			ThreadContext.put("LOG_KEY", "SUPPRESS");
			while (true) {
				try {
					logInfo(method, "checkinGateway ...");
					gatewayApi.checkin(gateway.getHid());
					break;
				} catch (Throwable e) {
					if (isShuttingDown()) {
						logInfo(method, "force terminating ...");
						break;
					} else {
						logWarn(method, "Check In Task FAILED!");
						logInfo(method, "Retrying in %d ms", EngineConstants.DEFAULT_GATEWAY_CHECK_IN_RETRY_MS);
						moonstone.acn.client.utils.Utils.sleep(EngineConstants.DEFAULT_GATEWAY_CHECK_IN_RETRY_MS);
					}
				}
			}
			ThreadContext.clearAll();
		}
	}

	private Set<Properties> loadPropsFromDB() {
		return DeviceService.getInstance().findAll().stream()
				.filter(device -> !Objects.equals(device.getType(), SelfInfo.DEFAULT_DEVICE_TYPE))
				.map(Utils::getProperties).collect(Collectors.toSet());
	}

	public Set<Properties> loadPropsFromFiles(String deviceDirectory) {
		String method = "loadPropsFromFiles";
		logInfo(method, "scanning device directory: %s", deviceDirectory);
		Set<Properties> props = new HashSet<>();
		File[] files = new File(deviceDirectory).listFiles((dir, name) -> name.endsWith(".properties"));
		if (files == null) {
			logError(method, "no device configuration found");
		} else {
			logInfo(method, "files size: %d", files.length);
			for (File file : files) {
				props.add(Utils.readPropertyFile(file.getAbsolutePath(), new Properties()));
			}
		}
		return props;
	}

	public void initDevices(Set<Properties> properties) {
		String method = "initDevices";
		ModuleService service = ModuleService.getInstance();
		Set<DeviceModule<?, ?, ?, ?>> deviceModules = loadModules(properties);
		for (DeviceModule<?, ?, ?, ?> module : deviceModules) {
			service.registerModule(module);
			service.startModule(module);
		}
		logInfo(method, "devices initialization finished, number of modules: %d", deviceModules.size());
	}

	@Override
	public void receive(String queue, byte[] message) {
		String method = "receive";
		SeleneEventModel model = JsonUtils.fromJsonBytes(message, SeleneEventModel.class);
		StatusModel result = new StatusModel().withId(model.getId()).withName(model.getName());
		switch (model.getName()) {
		case SeleneEventNames.GATEWAY_CHECK_STATUS: {
			result.withStatus("OK");
			break;
		}
		case SeleneEventNames.DEVICE_LOAD: {
			String uid = model.getParameters().get(DeviceLoadParams.DEVICE_UID);
			String type = model.getParameters().get(DeviceLoadParams.DEVICE_TYPE);
			Device device = DeviceService.getInstance().findByTypeAndUid(type, uid);
			if (device == null) {
				result.withStatus("ERROR")
						.withMessage(String.format("device of type: %s with uid: %s not found", type, uid));
			} else {
				try {
					Properties props = Utils.getProperties(device);
					String deviceClass = Utils.getRequiredProperty(props, EngineConstants.DEVICE_CLASS);
					if (!Objects.equals(deviceClass, SelfModule.class.getName())) {
						logInfo(method, "instantiating device class: %s", deviceClass);
						Object instance = Class.forName(deviceClass).getConstructor().newInstance();
						if (instance instanceof DeviceModule<?, ?, ?, ?>) {
							DeviceModule<?, ?, ?, ?> module = (DeviceModule<?, ?, ?, ?>) instance;
							module.init(props);
							ModuleService.getInstance().registerModule(module);
							result.withStatus("OK").withMessage("Device created successfully!");
						} else {
							logError(method, "device class is not of DeviceModule type");
							result.withStatus("ERROR").withMessage("Device class is not of DeviceModule type!");
						}
					}
				} catch (SeleneException ignored) {
					logError(method, "required: %s property not found", EngineConstants.DEVICE_CLASS);
					result.withStatus("ERROR").withMessage(
							String.format("required: %s property not found", EngineConstants.DEVICE_CLASS));
				} catch (ReflectiveOperationException e) {
					logError(method, "error instantiating class", e);
					result.withStatus("ERROR").withMessage("Error instantiating class!");
				}
			}
			break;
		}
		case SeleneEventNames.BLE_DISCOVERY: {
//            String bleInterface = model.getParameters().get(BleDiscoveryParams.INTERFACE);
//            long timeout = Long.valueOf(model.getParameters().get(BleDiscoveryParams.DISCOVERY_TIMEOUT));
//            try {
//                ensureLinuxEnvironment();
//                Map<String, String> discoveredDevices = BleDbusService.getInstance().discoverBleDevices(bleInterface,
//                        timeout);
//                Validate.notNull(discoveredDevices, "discoveredDevices is null");
//                logInfo(method, "Discovered Devices: %s", discoveredDevices.toString());
//                result.withStatus("OK").withMessage(JsonUtils.toJson(discoveredDevices));
//            } catch (Exception e) {
//                logError(method, "error discovering devices: %s", e);
//                result.withStatus("ERROR").withMessage("Error discovering BLE devices!");
//            }
			break;
		}
		case SeleneEventNames.DEVICE_UNLOAD: {
			String deviceUID = model.getParameters().get(DeviceLoadParams.DEVICE_UID);
			Device device = DeviceService.getInstance().findByUid(deviceUID);
			if (device == null) {
				result.withStatus("ERROR").withMessage(String.format("device with uid: %s not found", deviceUID));
			} else {
				try {
					Properties props = Utils.getProperties(device);
					String deviceClass = props.getProperty(EngineConstants.DEVICE_CLASS);
					if ((!Objects.equals(deviceClass, SelfModule.class.getName()))
							|| StringUtils.isEmpty(deviceClass)) {

						DeviceModule<?, ?, ?, ?> module = ModuleService.getInstance().findDevice(device.getHid());
						ModuleService.getInstance().stopModule(module);
						try {
							// if device was just created, then it wasn't
							// registered at all
							if (!Objects.equals(device.getStatus(), ModuleState.CREATED.name())) {
								getAcnClient().getDeviceApi().deleteDevice(device.getHid());
							}
							DeviceService.getInstance().delete(device);
							ModuleService.getInstance().deregisterModule(module);
							result.withStatus("OK");
						} catch (Exception e) {
							logError(method, "Fail to delete device.", e);
							result.withStatus("ERROR").withMessage("Fail to delete device.");
						}
					}
				} catch (SeleneException ignored) {
					logError(method, "required: %s property not found", EngineConstants.DEVICE_CLASS);
					result.withStatus("ERROR").withMessage(
							String.format("required: %s property not found", EngineConstants.DEVICE_CLASS));
				}
			}
			break;
		}
		case SeleneEventNames.GATEWAY_STOP: {

			// First stop all the modules
			ModuleService.getInstance().stopAllModules();
			// Then stop self module
			getDevice().setStatus(ModuleState.STOPPED.name());
			persistUpdatedDeviceInfo();

			result.withStatus("OK").withMessage("Gateway stopped");
			DatabusService.getInstance().send(model.getResponseQueue(), JsonUtils.toJsonBytes(result));
			moonstone.acn.client.utils.Utils.sleep(5);
			System.exit(0);
			break;
		}
		default: {
			logWarn(method, "command: %s is not supported", model.getName());
			result.withStatus("ERROR").withMessage(String.format("command: %s is not supported", model.getName()));
			break;
		}
		}
		DatabusService.getInstance().send(model.getResponseQueue(), JsonUtils.toJsonBytes(result));
	}

	private Set<DeviceModule<?, ?, ?, ?>> loadModules(Set<Properties> properties) {
		String method = "loadModules";
		Set<DeviceModule<?, ?, ?, ?>> modules = new HashSet<>();
		for (Properties props : properties) {
			String deviceClass;
			try {
				deviceClass = Utils.getRequiredProperty(props, EngineConstants.DEVICE_CLASS);
			} catch (RuntimeException ignored) {
				logWarn(method, "required: %s property not found", EngineConstants.DEVICE_CLASS);
				continue;
			}
			try {
				if (!Objects.equals(deviceClass, SelfModule.class.getName())) {
					logInfo(method, "instantiating device class: %s", deviceClass);
					Object instance = Class.forName(deviceClass).getConstructor().newInstance();
					if (instance instanceof DeviceModule<?, ?, ?, ?>) {
						DeviceModule<?, ?, ?, ?> module = (DeviceModule<?, ?, ?, ?>) instance;
						module.init(props);
						modules.add(module);
					} else {
						logError(method, "device class is not of DeviceModule type");
					}
				}
			} catch (ReflectiveOperationException e) {
				logError(method, "error instantiating class", e);
			}
		}
		return modules;
	}

	private void ensureLinuxEnvironment() {
		if (!SystemUtils.IS_OS_LINUX) {
			throw new SeleneException("Only Linux OS is supported for this feature at this time!");
		}
	}

	private class HealthCheckTimerTask extends TimerTask {
		private AtomicBoolean running = new AtomicBoolean(false);
		private File healthCheckScriptFile;
		private File healthCheckParserFile;

		private HealthCheckTimerTask() {
			String method = "HealthCheckTimerTask";
			String scriptFile = getProperties().getHealthCheckScriptFile();
			Validate.notBlank(scriptFile, "healthCheckScriptFile is empty");
			healthCheckScriptFile = new File(scriptFile);
			Validate.isTrue(healthCheckScriptFile.exists() && healthCheckScriptFile.isFile(),
					"invalid script file: " + scriptFile);

			String parserFile = getProperties().getHealthCheckParserFile();
			if (StringUtils.isNotEmpty(parserFile)) {
				healthCheckParserFile = new File(parserFile);
				Validate.isTrue(healthCheckParserFile.exists() && healthCheckParserFile.isFile(),
						"invalid parser file: " + parserFile);
			} else {
				logWarn(method, "healthCheckParserFile is not defined!");
			}
		}

		@Override
		public void run() {
			String method = "HealthCheckTimerTask.run";
			if (running.compareAndSet(false, true)) {
				try {
					queueDataForSending(checkHealth());
				} catch (Throwable e) {
					logError(method, e);
				} finally {
					running.set(false);
				}
			}
		}

		private SelfDataImpl checkHealth() throws SeleneException {
			String method = "HealthCheckTimerTask.checkHealth";
			logInfo(method, "checking health ...");
			ProcessStatusModel status = SysUtils.executeCommandLine(healthCheckScriptFile.getAbsolutePath());
			if (status.getExitCode() != 0) {
				logError(method, "command failed! exitCode: %d, error: %d, output: %s", status.getExitCode(),
						status.getError(), status.getOutput());
				throw new SeleneException(status.getError());
			} else {
				String output = StringUtils.trimToEmpty(status.getOutput());
				logInfo(method, "output length: %d", output.length());
				SelfDataImpl data = new SelfDataImpl();
				if (healthCheckParserFile != null) {
					data.withPayload(output.getBytes());
					logInfo(method, "parsing data with %s script", healthCheckParserFile);
					EngineProperties properties = ConfigService.getInstance().getEngineProperties();
					ScriptEngine engine = new ScriptEngineManager().getEngineByName(properties.getScriptingEngine());
					Validate.notNull(engine, "cannot find scripting engine");
					engine.put("data", data);
					engine.put("iotParams", new IotParameters());
					try (FileReader reader = new FileReader(healthCheckParserFile)) {
						engine.eval(reader);
						data.setParsedFully(true);
					} catch (Exception e) {
						throw new SeleneException("cannot execute health check parsing script", e);
					}
				} else {
					// output should be properly formatted
					try {
						Utils.populateDeviceData(data, JsonUtils.fromJson(output, MAP_TYPE_REF));
						data.setParsedFully(true);
					} catch (Exception e) {
						logError(method, "error parsing formatted payload", e);
						data.withPayload(output.getBytes());
					}
				}
				return data;
			}
		}
	}
}
