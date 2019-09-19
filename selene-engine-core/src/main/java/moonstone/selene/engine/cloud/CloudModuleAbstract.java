package moonstone.selene.engine.cloud;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acn.AcnEventNames.ServerToGateway;
import moonstone.acn.client.AcnClientException;
import moonstone.acn.client.IotParameters;
import moonstone.acn.client.api.CoreEventApi;
import moonstone.acn.client.api.DeviceApi;
import moonstone.acn.client.api.DeviceStateApi;
import moonstone.acn.client.api.GatewayApi;
import moonstone.acn.client.cloud.CloudConnectorAbstract;
import moonstone.acn.client.cloud.MessageListener;
import moonstone.acn.client.cloud.TransferMode;
import moonstone.acn.client.model.CloudPlatform;
import moonstone.acn.client.model.DeviceRegistrationModel;
import moonstone.acn.client.model.DeviceStateUpdateModel;
import moonstone.acn.client.model.SoftwareReleaseCommandModel;
import moonstone.acn.client.model.UpdateGatewayModel;
import moonstone.acn.client.utils.Utils;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.ErrorModel;
import moonstone.acs.client.model.GatewayEventModel;
import moonstone.selene.SeleneException;
import moonstone.selene.data.Device;
import moonstone.selene.databus.DatabusListener;
import moonstone.selene.databus.DatabusListenerAbstract;
import moonstone.selene.device.self.SelfModule;
import moonstone.selene.engine.DeviceModule;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.ModuleAbstract;
import moonstone.selene.engine.ModuleState;
import moonstone.selene.engine.service.DeviceService;
import moonstone.selene.engine.service.GatewayService;
import moonstone.selene.engine.service.ModuleService;
import moonstone.selene.engine.service.UpdateService;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateUpdate;
import moonstone.selene.service.DatabusService;

public abstract class CloudModuleAbstract extends ModuleAbstract implements CloudModule, MessageListener {
	private static final String DEVICE_HID = "deviceHid";
	private static final String GATEWAY_HID = "gatewayHid";
	private static final String TRANS_HID = "transHid";
	private static final String PAYLOAD = "payload";
	private static final String DEVICE_HID_IS_MISSING_MSG = DEVICE_HID + " is missing";

	private static final TypeReference<Map<String, State>> STATE_TYPE_REF = new TypeReference<Map<String, State>>() {
	};

	protected CloudConnectorAbstract connector;
	private final CloudPlatform cloudPlatform;
	private final DatabusListener telemetryListener = new TelemetryListener(getClass().getName());
	private final DatabusListener stateListener = new StateListener(getClass().getName());
	private final DatabusListener gatewayErrorListener = new GatewayErrorListener(getClass().getName());
	private final DatabusListener deviceErrorListener = new DeviceErrorListener(getClass().getName());
	private final DeviceApi deviceApi = SelfModule.getInstance().getAcnClient().getDeviceApi();
	private final GatewayApi gatewayApi = SelfModule.getInstance().getAcnClient().getGatewayApi();
	private Thread localThread;
	private List<IotParameters> telemetryBuffer = new ArrayList<>();
	private Map<String, ErrorModel> deviceErrorBuffer = new HashMap<>();
	private Map<String, ErrorModel> gatewayErrorBuffer = new HashMap<>();

	private TransferMode transferMode = TransferMode.BATCH;
	private long batchSendingIntervalMs;
	private Timer batchTimer;

	protected CloudModuleAbstract(CloudPlatform cloudPlatform) {
		this.cloudPlatform = cloudPlatform;
	}

	@Override
	public void start() {
		super.start();
		String method = "CloudModuleAbstract.start";

		SelfModule self = SelfModule.getInstance();

		String cloudTransferMode = self.getProperties().getCloudTransferMode();
		try {
			transferMode = TransferMode.valueOf(cloudTransferMode);
		} catch (IllegalArgumentException e) {
			logWarn(method, "bad cloudTransferMode value: %s", cloudTransferMode);
		}

		batchSendingIntervalMs = self.getProperties().getCloudBatchSendingIntervalMs();

		localThread = new Thread(this::startClient, getClass().getSimpleName());
		localThread.start();
		setState(ModuleState.STARTED);
	}

	protected void startClient() {
		String method = "CloudModuleAbstract.startClient";
		if (SelfModule.getInstance().getGateway().getCloudPlatform() == cloudPlatform) {
			DatabusService.getInstance().registerListener(telemetryListener, EngineConstants.DEFAULT_TELEMETRY_QUEUE);
			DatabusService.getInstance().registerListener(stateListener, EngineConstants.DEFAULT_STATE_QUEUE);
			if (isBatchSendingMode()) {
				batchTimer = new Timer("batchTimer", true);
				batchTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						processBatch();
					}
				}, 0L, batchSendingIntervalMs);
				logInfo(method, "batchTimer started");
			} else {
				logWarn(method, "batchSending is disabled");
			}
		} else {
			logWarn(method, "module is not active: %s", getName());
		}

		if (getClass() == IotConnectModule.class) {
			DatabusService.getInstance().registerListener(gatewayErrorListener,
					EngineConstants.DEFAULT_GATEWAY_ERROR_QUEUE);
			DatabusService.getInstance().registerListener(deviceErrorListener,
					EngineConstants.DEFAULT_DEVICE_ERROR_QUEUE);
		}

		logInfo(method, "databus started");

		Validate.notNull(connector, "connector is not initialized!");
		connector.setListener(this);
	}

	private class TelemetryListener extends DatabusListenerAbstract {

		TelemetryListener(String name) {
			super(name);
		}

		@Override
		public void receive(String queue, byte[] message) {
			String method = "receive";
			while (true) {
				try {
					IotParameters params = SerializationUtils.deserialize(message);
					if (isDebugEnabled()) {
						debugMessage(params);
					}
					if (isBatchSendingMode()) {
						queueForBatch(params);
					} else {
						connector.send(params);
					}
					break;
				} catch (Exception e) {
					logError("error publishing data", e);
					logInfo(method, "retrying in %d ms", EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
					Utils.sleep(EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
				}
			}
		}
	}

	private class StateListener extends DatabusListenerAbstract {

		StateListener(String name) {
			super(name);
		}

		@Override
		public void receive(String queue, byte[] message) {
			String method = "receive";
			StateUpdate states = JsonUtils.fromJsonBytes(message, StateUpdate.class);
			String hid = states.getHid();
			DeviceStateUpdateModel model = (DeviceStateUpdateModel) new DeviceStateUpdateModel()
					.withStates(states.getStates()).withTimestamp(Instant.now());
			DeviceStateApi deviceStateApi = SelfModule.getInstance().getAcnClient().getDeviceStateApi();
			while (true) {
				try {
					deviceStateApi.createStateUpdate(hid, model);
					break;
				} catch (Exception e) {
					logError("error publishing states", e);
					logInfo(method, "retrying in %d ms", EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
					Utils.sleep(EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
				}
			}
		}
	}

	private class GatewayErrorListener extends DatabusListenerAbstract {
		private final GatewayApi gatewayApi = SelfModule.getInstance().getAcnClient().getGatewayApi();

		public GatewayErrorListener(String name) {
			super(name);
		}

		@Override
		public void receive(String queue, byte[] message) {
			String method = "receive";
			if (SelfModule.getInstance().getProperties().isSendGatewayErrors()) {
				while (true) {
					try {
						Pair<String, ErrorModel> pair = SerializationUtils.deserialize(message);
						if (isBatchSendingMode()) {
							queueGatewayErrorForBatch(pair.getLeft(), pair.getRight());
						} else {
							gatewayApi.sendError(pair.getLeft(), pair.getRight());
						}
						break;
					} catch (Exception e) {
						logInfo(method, "retrying in %d ms", EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
						Utils.sleep(EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
					}
				}
			}
		}
	}

	private class DeviceErrorListener extends DatabusListenerAbstract {
		private final DeviceApi deviceApi = SelfModule.getInstance().getAcnClient().getDeviceApi();

		DeviceErrorListener(String name) {
			super(name);
		}

		@Override
		public void receive(String queue, byte[] message) {
			String method = "receive";
			if (SelfModule.getInstance().getProperties().isSendDeviceErrors()) {
				while (true) {
					try {
						Pair<String, ErrorModel> pair = SerializationUtils.deserialize(message);
						if (isBatchSendingMode()) {
							queueDeviceErrorForBatch(pair.getLeft(), pair.getRight());
						} else {
							deviceApi.sendError(pair.getLeft(), pair.getRight());
						}
						break;
					} catch (Exception e) {
						logInfo(method, "retrying in %d ms", EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
						Utils.sleep(EngineConstants.DEFAULT_CLOUD_SENDING_RETRY_MS);
					}
				}
			}
		}
	}

	@Override
	public void stop() {
		String method = "CloudModuleAbstract.stop";
		super.stop();
		if (batchTimer != null) {
			logInfo(method, "stopping batchTimer ...");
			batchTimer.cancel();
		}
		logInfo(method, "shutting down localThread ...");
		Utils.shutdownThread(localThread);
	}

	protected synchronized void queueForBatch(IotParameters parameters) {
		if (parameters != null) {
			telemetryBuffer.add(parameters);
		}
	}

	protected synchronized void queueDeviceErrorForBatch(String hid, ErrorModel errorModel) {
		if (errorModel != null) {
			deviceErrorBuffer.put(hid, errorModel);
		}
	}

	protected synchronized void queueGatewayErrorForBatch(String hid, ErrorModel errorModel) {
		if (errorModel != null) {
			gatewayErrorBuffer.put(hid, errorModel);
		}
	}

	protected synchronized void processBatch() {
		String method = "CloudModuleAbstract.processBatch";
		try {
			logDebug(method, "telemetryBuffer size: %d", telemetryBuffer.size());
			logDebug(method, "deviceErrorBuffer size: %d", deviceErrorBuffer.size());
			logDebug(method, "gatewayErrorBuffer size: %d", gatewayErrorBuffer.size());
			if (!telemetryBuffer.isEmpty()) {
				connector.sendBatch(telemetryBuffer, transferMode);
				telemetryBuffer.clear();
			}
		} catch (Exception e) {
			logError(method, "error process telemetry batch!", e);
		}
		try {
			if (!deviceErrorBuffer.isEmpty()) {
				for (Entry<String, ErrorModel> entry : deviceErrorBuffer.entrySet()) {
					deviceApi.sendError(entry.getKey(), entry.getValue());
				}
			}
		} catch (AcnClientException e) {
			logError(method, "error sending device error!", e);
		} finally {
			deviceErrorBuffer.clear();
		}
		try {
			if (!gatewayErrorBuffer.isEmpty()) {
				for (Entry<String, ErrorModel> entry : gatewayErrorBuffer.entrySet()) {
					gatewayApi.sendError(entry.getKey(), entry.getValue());
				}
			}
		} catch (AcnClientException e) {
			logError(method, "error sending gateway error!", e);
		} finally {
			gatewayErrorBuffer.clear();
		}
	}

	@Override
	public void processMessage(String topic, byte[] payload) {
		String method = "CloudModuleAbstract.processCloudMessage";

		String message = new String(payload, StandardCharsets.UTF_8);

		GatewayEventModel model = JsonUtils.fromJson(message, GatewayEventModel.class);

		if (model == null || StringUtils.isEmpty(model.getName())) {
			logError(method, "ignore invalid payload: %s", payload);
			throw new SeleneException(String.format("ignore invalid payload: %s", payload));
		}

		CoreEventApi eventApi = SelfModule.getInstance().getAcnClient().getCoreEventApi();
		eventApi.putReceived(model.getHid());
		try {
			switch (model.getName()) {
			case ServerToGateway.DEVICE_START: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				deviceModule.getProperties().setEnabled(true);
				Device device = deviceModule.getDevice();
				device.setProperties(JsonUtils.toJson(deviceModule.exportProperties()));
				DeviceService.getInstance().save(device);
				ModuleService.getInstance().startModule(deviceModule);
				return;
			}
			case ServerToGateway.DEVICE_STOP: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				deviceModule.getProperties().setEnabled(false);
				Device device = deviceModule.getDevice();
				device.setProperties(JsonUtils.toJson(deviceModule.exportProperties()));
				DeviceService.getInstance().save(device);
				ModuleService.getInstance().stopModule(deviceModule);
				return;
			}
			case ServerToGateway.DEVICE_COMMAND: {
				logInfo(method, "sending device commands ...");
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				if (!ModuleService.getInstance().performCommand(deviceModule, model.getParameters())) {
					throw new SeleneException("Failed to perform command");
				}
				return;
			}
			case ServerToGateway.DEVICE_PROPERTY_CHANGE: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				if (!ModuleService.getInstance().changeProperties(deviceModule, model.getParameters())) {
					throw new SeleneException("Failed to change device properties");
				}
				return;
			}
			case ServerToGateway.SENSOR_PROPERTY_CHANGE: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				if (!ModuleService.getInstance().changeSensorProperties(deviceModule, model.getParameters())) {
					throw new SeleneException("Failed to change sensor properties");
				}
				return;
			}
			case ServerToGateway.DEVICE_STATE_REQUEST: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				String deviceHid = model.getParameters().get(DEVICE_HID);
				DeviceStateApi deviceStateApi = SelfModule.getInstance().getAcnClient().getDeviceStateApi();
				String transHid = model.getParameters().get(TRANS_HID);
				deviceStateApi.transReceived(deviceHid, transHid);
				try {
					logInfo(method, "updating states ...");
					if (ModuleService.getInstance().updateStates(deviceModule,
							JsonUtils.fromJson(model.getParameters().get(PAYLOAD), STATE_TYPE_REF))) {
						deviceStateApi.transSucceeded(deviceHid, transHid);
					} else {
						deviceStateApi.transFailed(deviceHid, transHid, "Failed to update states");
					}
				} catch (Exception e) {
					deviceStateApi.transFailed(deviceHid, transHid, e.getMessage());
				}
				return;
			}
			case ServerToGateway.GATEWAY_CONFIGURATION_UPDATE: {
				logInfo(method, "received request to update gateway configuration");
				String gatewayHid = model.getParameters().get(GATEWAY_HID);
				gatewayApi.updateExistingGateway(gatewayHid, GatewayService.getInstance()
						.populateModel(GatewayService.getInstance().findOne(), new UpdateGatewayModel()));
				return;
			}
			case ServerToGateway.DEVICE_CONFIGURATION_UPDATE: {
				DeviceModule<?, ?, ?, ?> deviceModule = findDevice(model);
				logInfo(method, "received request to update device configuration");
				deviceApi.createOrUpdate(deviceModule.populate(new DeviceRegistrationModel()));
				return;
			}
			case ServerToGateway.GATEWAY_SOFTWARE_RELEASE: {
				try {
					SoftwareReleaseCommandModel command = populate(new SoftwareReleaseCommandModel(),
							model.getParameters());
					UpdateService.getInstance().updateGatewaySoftware(command);
					eventApi.putSucceeded(model.getHid());
				} catch (Exception e) {
					eventApi.putFailed(model.getHid(),
							String.format("gateway software upgrade failed: %s", e.getMessage()));
				}
				return;
			}
			case ServerToGateway.DEVICE_SOFTWARE_RELEASE: {
				SoftwareReleaseCommandModel command = populate(new SoftwareReleaseCommandModel(),
						model.getParameters());
				DeviceModule<?, ?, ?, ?> device = ModuleService.getInstance().findDevice(command.getHid());
				if (device == null) {
					eventApi.putFailed(model.getHid(), "Device not found: " + command.getHid());
				} else {
					try {
						UpdateService.getInstance().updateDeviceSoftware(command, device);
						eventApi.putSucceeded(model.getHid());
					} catch (Exception e) {
						eventApi.putFailed(model.getHid(),
								String.format("device software upgrade failed: %s", e.getMessage()));
					}
				}
				return;
			}
			case ServerToGateway.SENSOR_TELEMETRY_CHANGE: {
				String deviceHid = model.getParameters().get(DEVICE_HID);
				Validate.notBlank(deviceHid, DEVICE_HID_IS_MISSING_MSG);
				DeviceModule<?, ?, ?, ?> device = ModuleService.getInstance().findDevice(deviceHid);
				if (device == null) {
					eventApi.putFailed(model.getHid(), "Device not found: " + deviceHid);
				} else if (ModuleService.getInstance().changeSensorTelemetry(device, model.getParameters())) {
					eventApi.putSucceeded(model.getHid());
				} else {
					eventApi.putFailed(model.getHid(), "failed to change sensor telemetry");
				}
				return;
			}

			/*
			 * case ServerToGateway.GATEWAY_CONFIGURATION_RESTORE: { logInfo(method,
			 * "received request to restore gateway configuration"); UpdateGatewayModel
			 * gatewayModel = JsonUtils.fromJson(model.getParameters().get(PAYLOAD),
			 * UpdateGatewayModel.class);
			 * GatewayService.getInstance().populateFromModel(gatewayModel); String
			 * gatewayHid = model.getParameters().get(GATEWAY_HID);
			 * gatewayApi.updateExistingGateway(gatewayHid, gatewayModel); return; } case
			 * ServerToGateway.DEVICE_CONFIGURATION_RESTORE: { DeviceModule<?, ?, ?, ?>
			 * deviceModule = findDevice(model); DeviceRegistrationModel
			 * deviceRegistrationModel = JsonUtils.fromJson(
			 * model.getParameters().get(PAYLOAD), DeviceRegistrationModel.class);
			 * logInfo(method, "received request to restore device configuration");
			 * deviceModule.populateFromModel(deviceRegistrationModel);
			 * deviceModule.persistUpdatedDeviceInfo(); return; }
			 */
			default: {
				break;
			}
			}
		} catch (Throwable e) {
			logError(method, e);
			eventApi.putFailed(model.getHid(), e.getMessage());
			throw new SeleneException("Error processing cloud message", e);
		}
		logInfo(method, "not implemented: %s", model.getName());
		eventApi.putFailed(model.getHid(), "Unsupported event: " + model.getName());
		throw new SeleneException(String.format("Unsupported event: %s", model.getName()));
	}

	private DeviceModule<?, ?, ?, ?> findDevice(GatewayEventModel model) {
		String deviceHid = model.getParameters().get(DEVICE_HID);
		Validate.notBlank(deviceHid, DEVICE_HID_IS_MISSING_MSG);
		DeviceModule<?, ?, ?, ?> deviceModule = ModuleService.getInstance().findDevice(deviceHid);
		if (deviceModule == null) {
			throw new SeleneException("Device not found: " + deviceHid);
		}
		return deviceModule;
	}

	private void debugMessage(IotParameters params) {
		String method = "CloudModuleAbstract.debugMessage";
		StringBuilder sb = new StringBuilder();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				if (sb.length() > 0) {
					sb.append(',');
				}
				sb.append(entry.getKey());
				sb.append(':');
				String value = entry.getValue();
				if (value != null && value.length() > 100) {
					value = value.substring(0, 100) + "...";
				}
				sb.append(value);
			}
		}
		logDebug(method, sb.toString());
	}

	protected boolean isBatchSendingMode() {
		return transferMode == TransferMode.BATCH || transferMode == TransferMode.GZIP_BATCH;
	}

	private SoftwareReleaseCommandModel populate(SoftwareReleaseCommandModel model, Map<String, String> map) {
		model.withFromSoftwareVersion(map.getOrDefault("fromSoftwareVersion", model.getFromSoftwareVersion()));
		model.withHid(map.getOrDefault("hid", model.getHid()));
		model.withSoftwareReleaseTransHid(
				map.getOrDefault("softwareReleaseTransHid", model.getSoftwareReleaseTransHid()));
		model.withTempToken(map.getOrDefault("tempToken", model.getTempToken()));
		model.withToSoftwareVersion(map.getOrDefault("toSoftwareVersion", model.getToSoftwareVersion()));
		return model;
	}
}
