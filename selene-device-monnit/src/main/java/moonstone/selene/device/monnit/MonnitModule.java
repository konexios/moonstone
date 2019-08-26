package moonstone.selene.device.monnit;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.monnit.mine.EventHandling.GatewayResponseHandler;
import com.monnit.mine.MonnitMineAPI.Gateway;
import com.monnit.mine.MonnitMineAPI.GatewayMessage;
import com.monnit.mine.MonnitMineAPI.MineServer;
import com.monnit.mine.MonnitMineAPI.Sensor;
import com.monnit.mine.MonnitMineAPI.SensorMessage;
import com.monnit.mine.MonnitMineAPI.iExceptionHandler;
import com.monnit.mine.MonnitMineAPI.iGatewayMessageHandler;
import com.monnit.mine.MonnitMineAPI.iPersistGatewayHandler;
import com.monnit.mine.MonnitMineAPI.iPersistSensorHandler;
import com.monnit.mine.MonnitMineAPI.iSensorMessageHandler;
import com.monnit.mine.MonnitMineAPI.iUnknownGatewayHandler;
import com.monnit.mine.MonnitMineAPI.iUnknownSensorHandler;
import com.monnit.mine.MonnitMineAPI.enums.eFirmwareGeneration;
import com.monnit.mine.MonnitMineAPI.enums.eGatewayType;
import com.monnit.mine.MonnitMineAPI.enums.eMineListenerProtocol;
import com.monnit.mine.MonnitMineAPI.enums.eSensorApplication;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsUtils;
import moonstone.acs.JsonUtils;
import moonstone.selene.dao.DaoManager;
import moonstone.selene.device.monnit.handlers.GatewayStateChangeHandler;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.ModuleState;
import moonstone.selene.engine.Utils;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.ModuleService;
import moonstone.selene.model.StatusModel;

public class MonnitModule extends DeviceModuleAbstract<MonnitInfo, MonnitProperties, MonnitStates, MonnitData> {
	private final static String ALL_ADDRESSES = "0.0.0.0";

	private MineServer server;
	private Gateway gateway;
	private Map<Long, GenericSensorModule> devices = new ConcurrentHashMap<>();
	
	private final GatewayStateChangeHandler gatewayStateChangeHandler;
	
	public MonnitModule() {
		gatewayStateChangeHandler = new GatewayStateChangeHandler();
		handlers = Collections.singletonList(gatewayStateChangeHandler);
	}

	@Override
	protected void startDevice() {
		String method = "startDevice";

		logInfo(method, "MineServer version %s ", MineServer.getVersion());

		super.startDevice();
		try {
			server = new MineServer(eMineListenerProtocol.valueOf(getInfo().getProtocol()),
					InetAddress.getByName(getInfo().getListeningAddress()), getInfo().getListeningPort());
			server.StartServer();
			initServer();
			registerGateway();
			loadSensorModules();
		} catch (Exception e) {
			logError(method, "cannot start server", e);
		}
	}

	@Override
	public void stop() {
		String method = "stop";
		try {
			server.StopServer();
		} catch (Exception e) {
			logError(method, "cannot stop server", e);
		}
		super.stop();
	}

	public MineServer getServer() {
		return server;
	}

	@Override
	protected MonnitProperties createProperties() {
		return new MonnitProperties();
	}

	@Override
	protected MonnitInfo createInfo() {
		return new MonnitInfo();
	}

	@Override
	protected MonnitStates createStates() {
		return new MonnitStates();
	}

	private void initServer() {
		String method = "initServer";
		logInfo(method, "initialization Mine server...");
		try {
			if (server.addUnknownGatewayHandler(new UnknownGatewayHandler())) {
				logInfo(method, "added UnknownGatewayHandler");
			} else {
				logError(method, "failed to add UnknownGatewayHandler");
			}

			if (server.addUnknownSensorHandler(new UnknownSensorHandler())) {
				logInfo(method, "added UnknownSensorHandler");
			} else {
				logError(method, "failed to add UnknownSensorHandler");
			}

			if (server.addGatewayDataProcessingHandler(new GatewayMessageHandler())) {
				logInfo(method, "added GatewayMessageHandler");
			} else {
				logError(method, "failed to add GatewayMessageHandler");
			}

			if (server.addSensorDataProcessingHandler(new SensorMessageHandler())) {
				logInfo(method, "added SensorMessageHandler");
			} else {
				logError(method, "failed to add SensorMessageHandler");
			}

			if (server.addExceptionProcessingHandler(new ExceptionProcessingHandler())) {
				logInfo(method, "added ExceptionProcesingHandler");
			} else {
				logInfo(method, "failed to add ExceptionProcesingHandler");
			}

			if (server.addPersistGatewayHandler(new PersistGatewayHandler())) {
				logInfo(method, "added PersistGatewayHandler");
			} else {
				logInfo(method, "failed to add PersistGatewayHandler");
			}

			if (server.addPersistSensorHandler(new PersistSensorHandler())) {
				logInfo(method, "added PersistSensorHandler");
			} else {
				logInfo(method, "failed to add PersistSensorHandler");
			}

			server.addGatewayResponseHandler(new GatewayResponseHandler());
			server.ShowDebugMessages(true);
			logInfo(method, "successfully initialized Mine server");
		} catch (InterruptedException e) {
			logError(method, "failed to initialize Mine server", e);
		}
	}

	private void registerGateway() {
		String method = "registerGateway";
		MonnitInfo info = getInfo();
		MonnitProperties props = getProperties();
		long gatewayId = info.getGatewayId();
		logInfo(method, "registering gateway %06d...", gatewayId);
		if (gatewayId > 0) {
			try {
				eGatewayType gatewayType = eGatewayType.valueOf(info.getGatewayType());
				gateway = new Gateway(gatewayId, gatewayType, info.getGatewayFirmwareVersion(),
						info.getRadioFirmwareVersion(), ALL_ADDRESSES, info.getListeningPort());
				gateway.setNetworkListInterval(props.getGatewayNetworkListInterval());
				gateway.setObserveAware(props.isGatewayObserveAware());
				gateway.IsDirty = false;
				server.RegisterGateway(gateway);
				logInfo(method, "gateway %06d registered successfully", gatewayId);

				// update gateway configuration
				logInfo(method, "updating gateway, pollInterval: %.2f, reportInterval: %.2f",
						props.getGatewayPollInterval(), props.getGatewayReportInterval());
				gateway.UpdatePollInterval(props.getGatewayPollInterval());
				gateway.UpdateReportInterval(props.getGatewayReportInterval());
				
				gatewayStateChangeHandler.setMonnitGateway(gateway);
				gatewayStateChangeHandler.setProperties(getProperties());
				gatewayStateChangeHandler.setMonnitServer(server);
			} catch (Exception e) {
				logError(method, "failed to register gateway %06d", gatewayId);
			}
		} else {
			logError(method, "invalid gatewayId: %d", gatewayId);
		}
	}

	private class UnknownGatewayHandler implements iUnknownGatewayHandler {
		@Override
		public void ProcessUnknownGateway(long i, eGatewayType eGatewayType) {
			String method = "ProcessUnknownGateway";
			logInfo(method, "unknown gateway %06d of type %s found", i, eGatewayType);
		}
	}

	private class UnknownSensorHandler implements iUnknownSensorHandler {
		@Override
		public void ProcessUnknownSensor(long id, eSensorApplication app) {
			String method = "ProcessUnknownSensor";
			logInfo(method, "unknown sensor %06d of type %s found", id, app.name());
		}
	}

	private class GatewayMessageHandler implements iGatewayMessageHandler {
		@Override
		public void ProcessGatewayMessage(GatewayMessage gatewayMessage) {
			String method = "ProcessGatewayMessage";
			logInfo(method, "Begin processing of GatewayMessage: %s", gatewayMessage.toString());

			boolean sendUpdate = false;
			double pollingInterval = gateway.getPollInterval();
			double reportInterval = gateway.getReportInterval();
			logInfo(method, "---> gateway pollInterval: %.2f, gateway reportInterval: %.2f", pollingInterval,
					reportInterval);
			logInfo(method, "---> properties pollInterval: %.2f, properties reportInterval: %.2f",
					getProperties().getGatewayPollInterval(), getProperties().getGatewayReportInterval());

			if (getProperties().getGatewayPollInterval() != pollingInterval) {
				logInfo(method,
						"Set gateway polling interval from current %d seconds to match properties file %d seconds",
						pollingInterval, getProperties().getGatewayPollInterval());
				// getProperties().setGatewayPollInterval(pollingInterval);
				gateway.UpdatePollInterval(getProperties().getGatewayPollInterval());
				sendUpdate = true;
			} else {
				logInfo(method, "Gateway polling interval matches polling interval specified in properties file");
			}

			if (getProperties().getGatewayReportInterval() != reportInterval) {
				// getProperties().setGatewayReportInterval(reportInterval);
				logInfo(method, "Gateway report interval does not match report interval specified in properties file");

				logInfo(method,
						"Gateway report interval of " + reportInterval + " minutes does not match report interval of "
								+ getProperties().getGatewayReportInterval() + " minutes specified in properties file");
				logInfo(method,
						"Setting gateway report interval from current " + reportInterval
								+ " minutes to match report interval specified in properties file of  "
								+ getProperties().getGatewayReportInterval() + " minutes");

				gateway.UpdateReportInterval(getProperties().getGatewayReportInterval());

				logInfo(method, "Gateway update report interval completed");
				sendUpdate = true;
			} else {
				logInfo(method, "Gateway report interval matches report interval specified in properties file");
			}

			if (sendUpdate) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						logInfo(method, "sending gateway update to cloud ...");
						persistUpdatedDeviceInfo();
					}
				}).start();
			}
		}
	}

	private class ExceptionProcessingHandler implements iExceptionHandler {
		@Override
		public void LogException(Exception exception) {
			String method = "LogException";
			logError(method, exception);
		}
	}

	private class PersistGatewayHandler implements iPersistGatewayHandler {
		@Override
		public void ProcessPersistGateway(long id) {
			String method = "ProcessPersistGateway";
			logInfo(method, "id: %d", id);
		}
	}

	private class PersistSensorHandler implements iPersistSensorHandler {
		@Override
		public void ProcessPersistSensor(long id) {
			String method = "ProcessPersistSensor";
			logInfo(method, "id: %d", id);
		}
	}

	private class SensorMessageHandler implements iSensorMessageHandler {
		@Override
		public void ProcessSensorMessages(List<SensorMessage> list, Gateway gateway) throws Exception {
			String method = "ProcessSensorMessages";
			long gatewayId = gateway.getGatewayID();
			logInfo(method, "gatewayId: %d, list size: %d", gatewayId, list.size());
			if (gatewayId == getInfo().getGatewayId()) {
				for (SensorMessage sensorMessage : list) {
					try {
						Long sensorId = sensorMessage.getSensorID();
						logInfo(method, "sensorId: %s, profileId: %s, state: %s, voltage: %s", sensorId,
								sensorMessage.getProfileID(), sensorMessage.getState(), sensorMessage.getVoltage());
						Sensor sensor = server.FindSensor(sensorId);
						if (sensor == null) {
							logWarn(method, "[%s] sensor not found on MineServer!", sensorId);
						} else {
							logInfo(method, "[%s] found sensor on MineServer: %s", sensorId,
									sensor.getMonnitApplication().name());
						}
						GenericSensorModule module = devices.get(sensorId);
						if (module == null) {
							// double-check locking
							synchronized (this) {
								if (module == null) {
									if (sensor != null) {
										logInfo(method, "[%s] creating new device module", sensorId);
										module = createModule(gatewayId, sensorId, sensorMessage.getProfileID());
										logInfo(method, "[%s] created new device module", sensorId);
										getInfo().getSensors().getValues().add(new GenericSensorInfo(sensorId,
												sensor.getMonnitApplication().Value(),
												sensor.getMonnitApplication().name(), sensor.getFirmwareVersion(),
												sensor.getGenerationType().name(), sensor.getReportInterval()));
										logInfo(method, "[%s] sending data to cloud ...", sensorId);
										persistUpdatedDeviceInfo();
										logInfo(method, "[%s] created new device module", sensorId);
										devices.put(sensorId, module);
									} else {
										logWarn(method,
												"[%s] can't create module since sensor is not found on MineServer!",
												sensorId);
									}
								}
							}
						}
						if (module != null && module.getState() == ModuleState.STARTED) {
							logInfo(method, "Begins processing sensorMessage %s", sensorMessage.toString());
							module.processSensorMessage(sensor, sensorMessage);
							logInfo(method, "Completes processing sensorMessage %s", sensorMessage.toString());
						} else {
							logWarn(method, "[%d] message is ignored!", sensorId);
						}
					} catch (Exception e) {
						logError(method, e);
					}
				}
			} else {
				logDebug(method, "ignored message from different gatewayId: %d", gatewayId);
			}
		}
	}

	private GenericSensorModule createModule(long gatewayId, long sensorId, int profileId) throws Exception {
		String method = "createModule";
		eSensorApplication application = eSensorApplication.ToApplication(profileId);
		logInfo(method, "[%d] application: %s", sensorId, application.name());
		GenericSensorModule genericSensorModule = new GenericSensorModule(this, sensorId, application);
		if (genericSensorModule.getDevice() == null) {
			genericSensorModule.init(new Properties());
			logInfo(method, "[%d] registering module ...", sensorId);
			ModuleService.getInstance().registerModule(genericSensorModule);
			logInfo(method, "[%d] starting module ...", sensorId);
			ModuleService.getInstance().startModule(genericSensorModule);
		}
		return genericSensorModule;
	}

	private void loadSensorModules() {
		String method = "loadSensorModules";
		logInfo(method, "sensorReportInterval: %s", getProperties().getSensorReportInterval());
		Map<String, Properties> properties = new HashMap<>();
		if (!DaoManager.getInstance().isFreshDatabase()) {
			properties = loadPropsFromDB();
		}

		if (isDebugEnabled()) {
			for (Entry<String, Properties> entry : properties.entrySet()) {
				Properties p = entry.getValue();
				for (String name : p.stringPropertyNames()) {
					logDebug(method, "%s: %s ---> %s", entry.getKey(), name, p.getProperty(name));
				}
			}
		}

		for (SensorInfoAbstract info : getInfo().getSensors().getValues()) {
			try {
				logInfo(method, "loading sensorId: %s ...", info.getSensorId());
				GenericSensorModule module = createModule(getInfo().getGatewayId(), info.getSensorId(),
						info.getProfileId());
				devices.put(info.getSensorId(), module);
				module.persistUpdatedDeviceInfo();
				Sensor sensor = new Sensor(info.getSensorId(), eSensorApplication.ToApplication(info.getProfileId()),
						info.getVersion(), eFirmwareGeneration.valueOf(info.getPlatform()));
				double reportInterval = info.getReportInterval();
				if (reportInterval == 0) {
					reportInterval = getProperties().getSensorReportInterval();
				}
				logInfo(method, "setting reportInterval to: %f", reportInterval);
				sensor.setReportInterval(reportInterval);
				logInfo(method, "registering sensorId: %s ...", info.getSensorId());
				server.RegisterSensor(getInfo().getGatewayId(), sensor);
				logInfo(method, "successfully registered sensorId: %s", info.getSensorId());
			} catch (Exception e) {
				String error = String.format("cannot initialize module for sensor %06d", info.getSensorId());
				logError(method, error, e);
			}
		}
	}

	private static Map<String, Properties> loadPropsFromDB() {
		return DeviceService.getInstance().find(GenericSensorInfo.DEFAULT_DEVICE_TYPE).stream()
				.filter(device -> device.getHid() != null).map(Utils::getProperties)
				.collect(Collectors.toMap(props -> props.getProperty("uid"), props -> props));
	}

	@Override
	public StatusModel performCommand(byte... bytes) {
		String method = "performCommand";
		super.performCommand(bytes);
		Map<String, String> params = JsonUtils.fromJsonBytes(bytes, EngineConstants.MAP_TYPE_REF);
		StatusModel result = StatusModel.OK;
		String command = params.get("command");
		logInfo(method, "command: %s", command);
		switch (command) {
		case "registerSensor": {
			String payload = params.get("payload");
			logInfo(method, "payload: %s", payload);
			GenericSensorInfo info = JsonUtils.fromJson(payload, GenericSensorInfo.class);
			try {
				eSensorApplication app = null;
				int profileId = info.getProfileId();
				String profileName = info.getProfileName();
				if (profileId > 0) {
					app = eSensorApplication.ToApplication(profileId);
					logInfo(method, "profileId: %d ---> %s", profileId, app == null ? "NULL" : app.name());
				} else {
					if (StringUtils.isNotEmpty(profileName)) {
						app = eSensorApplication.valueOf(profileName);
						logInfo(method, "profileName: %s ---> %s", profileName, app == null ? "NULL" : app.name());
					}
				}
				if (app == null) {
					throw new AcsLogicalException("Invalid profileId/profileName: " + profileId + "/" + profileName);
				}

				String platform = info.getPlatform();
				eFirmwareGeneration generation = eFirmwareGeneration.valueOf(platform);
				logInfo(method, "platform: %s ---> %s", platform, generation.name());

				double reportInterval = info.getReportInterval();
				if (reportInterval == 0) {
					reportInterval = getProperties().getSensorReportInterval();
				}
				logInfo(method, "reportInterval: %f", reportInterval);

				long sensorId = info.getSensorId();
				logInfo(method, "sensorId: %d", sensorId);

				String version = info.getVersion();
				logInfo(method, "version: %s", version);

				registerSensor(sensorId, app, version, generation, reportInterval);
			} catch (Exception e) {
				logError(method, "failed to register sensor", e);
				result.withStatus("ERROR")
						.withMessage(String.format("failed to register sensor \n %s", AcsUtils.getStackTrace(e)));
			}
			break;
		}
		case "changeGatewayReportInterval": {
			String payload = params.get("payload");
			logInfo(method, "payload: %s", payload);
			GenericSensorInfo info = JsonUtils.fromJson(payload, GenericSensorInfo.class);
			logInfo(method, "Current gateway report interval %s", gateway.getReportInterval());
			logInfo(method, "New gateway report interval %s", info.getReportInterval());
			double reportInterval = info.getReportInterval();
			double gatewayReportInterval = gateway.getReportInterval();
			logInfo(method, "Changing gateway report interval from %f minutes to %f minutes", gatewayReportInterval,
					reportInterval);
			getProperties().setGatewayReportInterval(info.getReportInterval());
			logInfo(method, "Completed changing gateway report interval from %f minutes to %f minutes",
					gatewayReportInterval, reportInterval);
			break;
		}
		case "changeSensorReportInterval": {
			String payload = params.get("payload");
			logInfo(method, "payload: %s", payload);
			GenericSensorInfo info = JsonUtils.fromJson(payload, GenericSensorInfo.class);
			try {
				Sensor sensor = server.FindSensor(info.getSensorId());
				logInfo(method, "Current sensor report interval %s", sensor.getReportInterval());
				logInfo(method, "New sensor report interval %s", info.getReportInterval());
				double reportInterval = info.getReportInterval();
				double sensorReportInterval = sensor.getReportInterval();
				logInfo(method, "Changing sensor report interval from %f minutes to %f minutes", sensorReportInterval,
						reportInterval);
				getProperties().setSensorReportInterval(reportInterval);
				logInfo(method, "Completed changing sensor report interval from %f minutes to %f minutes",
						sensorReportInterval, reportInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		}
		return result;
	}

	@Override
	public void notifyPropertiesChanged(Map<String, String> properties) {
		String method = "notifyPropertiesChanged";
		for (String name : properties.keySet()) {
			logInfo(method, "name: %s ---> value: %s", name, properties.get(name));
		}
		super.notifyPropertiesChanged(properties);
		gateway.UpdatePollInterval(getProperties().getGatewayPollInterval());
		gateway.UpdateReportInterval(getProperties().getGatewayReportInterval());
	}

	private void registerSensor(Long sensorId, eSensorApplication app, String version, eFirmwareGeneration generation,
			Double reportInterval) throws Exception {
		String method = "registerSensor";
		Sensor sensor = new Sensor(sensorId, app, version, generation);
		sensor.setReportInterval(reportInterval);
		logInfo(method, "registering sensor: %d/%s ...", sensorId, app.name());
		server.RegisterSensor(getInfo().getGatewayId(), sensor);
		logInfo(method, "registering completed for sensor: %d/%s", sensorId, app.name());
	}
}
