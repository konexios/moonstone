package moonstone.selene.device.ble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import moonstone.selene.device.ble.gatt.ConnectionHandler;
import moonstone.selene.device.ble.gatt.Gatt;
import moonstone.selene.device.ble.gatt.GattFactory;
import moonstone.selene.device.ble.gatt.NotificationHandler;
import moonstone.selene.device.ble.sensor.BleSensor;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.sensor.SensorDataImpl;
import moonstone.selene.engine.DeviceModuleAbstract;

public abstract class BleModuleAbstract<Info extends BleInfo, Prop extends BleProperties, State extends BleStates, Data extends BleData>
		extends DeviceModuleAbstract<Info, Prop, State, Data>
		implements BleModule<Info, Prop, State, Data>, NotificationHandler, ConnectionHandler {

	private Map<String, BleSensor<?, ?>> sensorMap = new HashMap<>();
	private List<BleSensor<?, ?>> passiveSensors = new ArrayList<>();
	private Map<String, BleSensor<?, ?>> controlMap = new HashMap<>();
	private Map<String, String> handleToCharUuid = new HashMap<>();

	private Gatt gatt;
	private Timer pollingTimer;
	private Timer sendingTimer;

	private Map<String, SensorData<?>> sensorDataMap = new HashMap<>();

	@Override
	protected void startDevice() {
		super.startDevice();
		String method = "BleModuleAbstract.startDevice";
		logInfo(method, "isUseDbus: %s", getProperties().isUseDbus());
		gatt = GattFactory.initGatt(this, getProperties().isUseDbus());
		gatt.setConnectionHandler(this);
		gatt.connect(getInfo().getBleInterface(), getInfo().getBleAddress(), getProperties().getRetryInterval(),
				isRandomAddress());
		gatt.setNotificationHandler(this);
	}

	@Override
	public void stop() {
		String method = "BleModuleAbstract.stop";
		logInfo(method, "disconnecting...");
		stopTimers();
		gatt.disconnect();
		super.stop();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleNotification(String handle, byte[] bytes) {
		String method = "BleModuleAbstract.handleNotification";
		logDebug(method, "handle: %s value: %s", handle, Arrays.toString(bytes));
		if (gatt.isConnected()) {
			BleSensor<?, ?> sensor = sensorMap.get(getCharacteristic(handle));
			if (sensor != null) {
				stageForSending((List<SensorData<?>>) sensor.parseData(bytes));
			} else {
				// TODO handle error
				logError(method, "handle not supported: %s", handle);
			}
		} else {
			logInfo(method, "WARNING: gatt is no longer connected, could be erroneous data!");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleNotification(BleSensor<?, ?> sensor, byte[] bytes) {
		String method = "BleModuleAbstract.handleNotification";
		logDebug(method, "value: %s", Arrays.toString(bytes));
		logInfo(method, "value: %s", Arrays.toString(bytes));
		if (gatt.isConnected()) {
			stageForSending((List<SensorData<?>>) sensor.parseData(bytes));
		} else {
			logInfo(method, "WARNING: gatt is no longer connected, could be erroneous data!");
		}
	}

	@Override
	public void handleConnection() {
		String method = "handleConnection";

		logInfo(method, "populating device info from BLE ...");
		gatt.populateInfo(getInfo());
		persistUpdatedDeviceInfo();

		configureSensors(gatt);
		startTimers();
	}

	@Override
	public void handleDisconnection() {
		stopTimers();
	}

	protected void initSensor(Properties props, BleSensorAbstract<?, ?> sensor) {
		sensor.init(props);
		registerSensor(sensor);
	}

	protected void registerSensor(BleSensor<?, ?> sensor) {
		for (String uuid : sensor.getUUIDs()) {
			sensorMap.put(uuid, sensor);
		}
		for (String telemetryName : sensor.getControlledTelemetry()) {
			controlMap.put(telemetryName, sensor);
		}
		if (sensor.isPassive()) {
			passiveSensors.add(sensor);
		}
	}

	@Override
	public boolean isRandomAddress() {
		return false;
	}

	protected void configureSensors(Gatt gatt) {
		String method = "BleModuleAbstract.configureSensors";
		logInfo(method, "configuring sensors...");
		Map<String, String> charMap = gatt.getCharacteristicMap();
		sensorMap.forEach((uuid, bleSensor) -> {
			addHandleToChar(charMap.get(uuid), uuid);
			bleSensor.configure(gatt);
		});
		logInfo(method, "sensors configured");
	}

	@Override
	public Map<String, BleSensor<?, ?>> getSensors() {
		return Collections.unmodifiableMap(sensorMap);
	}

	@Override
	public Map<String, String> exportProperties() {
		Map<String, String> properties = super.exportProperties();
		for (BleSensor<?, ?> sensor : sensorMap.values()) {
			Map<String, String> sensorProperties = sensor.getProperties().exportProperties();
			sensorProperties
					.forEach((key, value) -> properties.put(String.format("%s/%s", sensor.getName(), key), value));
		}
		return properties;
	}

	@Override
	public void importProperties(Map<String, String> properties) {
		super.importProperties(properties);
		Map<String, String> sensorProperties = new HashMap<>();
		for (BleSensor<?, ?> sensor : sensorMap.values()) {
			String prefix = sensor.getName() + '/';
			sensorProperties.clear();
			properties.forEach((key, value) -> {
				if (key.startsWith(prefix)) {
					sensorProperties.put(key, value);
				}
			});
			if (!sensorProperties.isEmpty()) {
				sensor.getProperties().importProperties(sensorProperties);
			}
		}
	}

	@Override
	public void notifyPropertiesChanged(Map<String, String> properties) {
		super.notifyPropertiesChanged(properties);
		configureSensors(gatt);
	}

	// TODO will be removed soon
	@Override
	public void notifyTelemetryChanged(Map<String, String> properties) {
		String method = "BleModuleAbstract.notifyTelemetryChanged";
		super.notifyTelemetryChanged(properties);
		for (Entry<String, String> entry : properties.entrySet()) {
			BleSensor<?, ?> sensor = controlMap.get(entry.getKey());
			if (sensor == null) {
				logWarn(method, "unknown telemetry %s received", entry.getKey());
			} else {
				logInfo(method, "Set telemetry for sensor %s to value %s", sensor.getName(), entry.getValue());
				sensor.setTelemetry(entry.getValue());
			}
		}
	}

	@Override
	public void addHandleToChar(String handle, String characteristic) {
		handleToCharUuid.put(handle, characteristic);
	}

	@Override
	public String getCharacteristic(String handle) {
		return handleToCharUuid.get(handle);
	}

	private void startTimers() {
		String method = "startTimers";
		stopTimers();

		// start pollingTimer
		pollingTimer = new Timer();
		logInfo(method, "\n\nScheduling PollingTimerTask for execution every %s \n",
				getProperties().getSampleIntervalMs());
		pollingTimer.schedule(new PollingTimerTask(), 0L, getProperties().getSampleIntervalMs());
		logInfo(method, "pollingTimer started!");

		// start sendingTimer
		sendingTimer = new Timer();
		sendingTimer.schedule(new SendingTimerTask(), 0L, getProperties().getSampleIntervalMs());
		logInfo(method, "sendingTimer started!");
	}

	private void stopTimers() {
		if (pollingTimer != null) {
			pollingTimer.cancel();
			pollingTimer = null;
		}
		if (sendingTimer != null) {
			sendingTimer.cancel();
			sendingTimer = null;
		}
	}

	public void disableSensorsForFWUpgrade() {
		String method = "disableSensorsForFWUpgrade";
		logInfo(method, "disabling sensors for firmware upgrade");
		for (BleSensor<?, ?> sensor : sensorMap.values()) {
			if (sensor.isEnabled()) {
				sensor.disable();
				logDebug(method, "disabled ---------- " + sensor.getName() + " sensor to upgrade");
			}
		}
	}

	public void enableSensorsAfterFWUpgrade() {
		String method = "enableSensorsAfterFWUpgrade";
		logInfo(method, "enabling sensors after sucessful firmware upgrade");
		for (BleSensor<?, ?> sensor : sensorMap.values()) {
			// Enable sensors that were previously disabled
			if (sensor.getProperties().isEnabled()) {
				sensor.enable();
				logDebug(method, "enabled ---------- " + sensor.getName() + " sensor");
			}
		}
		logDebug(method, "enabled all sensors after firmware upgrade");
	}

	synchronized void stageForSending(List<SensorData<?>> data) {
		String method = "stageForSending";
		try {
			if (data != null && data.size() > 0)
				for (SensorData<?> item : data) {
					sensorDataMap.put(item.getName(), item);
				}
		} catch (Exception e) {
			logError(method, e);
		}
	}

	synchronized void flushData() {
		String method = "flushData";
		try {
			logInfo(method, "sensorDataMap size: %d", sensorDataMap.size());
			if (sensorDataMap.size() > 0) {
				final SensorDataImpl<SensorData<?>> dataToSend = new SensorDataImpl<>(
						new ArrayList<>(sensorDataMap.values()));
				getService().submit(() -> {
					queueDataForSending(dataToSend);
				});
				sensorDataMap.clear();
			}
		} catch (Exception e) {
			logError(method, e);
		}
	}

	class PollingTimerTask extends TimerTask {
		@Override
		public void run() {
			logInfo("PollingTimerTask ", "run() starts ...");
			passiveSensors.stream().filter(sensor -> sensor.getProperties().isEnabled()).forEach(sensor -> {
				byte[] data = sensor.readValue();

				StringBuffer dataValue = new StringBuffer();
				for (byte value : data) {
					dataValue.append(" " + value);
				}

				logInfo("PollingTimerTask ", sensor.getName() + " has value " + dataValue);

				if (!getProperties().isUseDbus() && data != null) {
					handleNotification(sensor, data);
				}
			});

			logInfo("PollingTimerTask ", "run() completes ...");
		}
	}

	class SendingTimerTask extends TimerTask {
		@Override
		public void run() {
			flushData();
		}
	}
}
