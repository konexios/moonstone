package com.arrow.selene.device.ble.specs.bluest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.arrow.selene.device.ble.BleData;
import com.arrow.selene.device.ble.BleInfo;
import com.arrow.selene.device.ble.BleModuleAbstract;
import com.arrow.selene.device.ble.BleProperties;
import com.arrow.selene.device.ble.BleStates;
import com.arrow.selene.device.ble.gatt.Gatt;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.simba.GattConstants;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.BlueST.Feature;
import com.arrow.selene.device.ble.specs.bluest.sensor.AccelerationEventSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.AccelerometerSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.ActivitySensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.CarryPositionSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.FusionCompactSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.GyrometerSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.HumiditySensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.LightSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.MagnetometerSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.MemsGestureSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.MicLevelSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.PressureSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.SwitchSensor;
import com.arrow.selene.device.ble.specs.bluest.sensor.TemperatureSensor;
import com.arrow.selene.device.sensor.SensorDataImpl;

public abstract class AbstractBlueSTModule<Info extends BleInfo, Prop extends BleProperties, State extends BleStates, Data extends BleData>
        extends BleModuleAbstract<Info, Prop, State, Data> {

	protected Gatt gatt = null;
	protected BlueST blueSt = new BlueST();
	protected Map<BlueST.Feature, BleSensorAbstract<?, ?>> sensorMap = new HashMap<>();
	private long lastTimestamp;

	public AbstractBlueSTModule() {
		sensorMap.put(Feature.PRESSURE, new PressureSensor());
		sensorMap.put(Feature.HUMIDITY, new HumiditySensor());
		sensorMap.put(Feature.TEMPERATURE, new TemperatureSensor());
		sensorMap.put(Feature.ACCELEROMETER, new AccelerometerSensor());
		sensorMap.put(Feature.GYROMETER, new GyrometerSensor());
		sensorMap.put(Feature.MAGNETOMETER, new MagnetometerSensor());
		sensorMap.put(Feature.LIGHT, new LightSensor());
		sensorMap.put(Feature.MIC_LEVEL, new MicLevelSensor());
		sensorMap.put(Feature.SENSOR_FUSION_COMPACT, new FusionCompactSensor());
		sensorMap.put(Feature.CARRY_POSITION, new CarryPositionSensor());
		sensorMap.put(Feature.SWITCH, new SwitchSensor());
		sensorMap.put(Feature.ACCELERATION_EVENT, new AccelerationEventSensor());
		sensorMap.put(Feature.ACTIVITY, new ActivitySensor());
		sensorMap.put(Feature.MEMS_GESTURE, new MemsGestureSensor());

		sensorMap.forEach((feature, sensor) -> blueSt.setSensor(feature, sensor));
	}

	@Override
	public void init(Properties props) {
		sensorMap.forEach((feature, sensor) -> initSensor(props, sensor));
		super.init(props);

		// Force use of dbus gatt because kura gatt does not support random
		// address type in ble
		getProperties().setUseDbus(true);
	}

	@Override
	public boolean isRandomAddress() {
		return true;
	}

	protected void doFirmwareUpgrade(byte[] bytes) {
		// not implemented
	}

	@Override
	public void handleNotification(String handle, byte[] bytes) {
		if (gatt == null)
			return;

		String method = "handleNotification";
		logDebug(method, "handle: %s value: %s", handle, Arrays.toString(bytes));

		if (gatt.isConnected()) {
			String uuid = getCharacteristic(handle);
			List<BleSensorAbstract<?, ?>> sensors = blueSt.getSensors(uuid);

			// remove the first two bytes of timestamp
			int offset = 2;
			long currentTimestamp = System.currentTimeMillis();

			BleSensorAbstract<?, ?> firmwareUpgrader = blueSt.getFirmwareUpgrader();

			if (firmwareUpgrader != null && sensors.contains(firmwareUpgrader)) {
				doFirmwareUpgrade(bytes);
			}
			// Stop handling too many notifications; skip notification if the
			// relative time-interval is 100 milliseconds
			else if (currentTimestamp - lastTimestamp > getProperties().getMaxPollingIntervalMs()) {
				for (BleSensorAbstract<?, ?> sensor : sensors) {
					if (sensor.getProperties().isEnabled()) {
						SensorDataImpl<?> sensorData = new SensorDataImpl<>(sensor.parseData(bytes, offset));
						getService().submit(() -> queueDataForSending(sensorData));
					}

					// update offset by bytes already used by sensor
					offset += sensor.getPayloadSize();
				}
				// update last timestamp
				lastTimestamp = currentTimestamp;
			}
		} else {
			logInfo(method, "WARNING: gatt is no longer connected, could be erroneous data!");
		}
	}

	@Override
	protected void configureSensors(Gatt gatt) {
		if (this.gatt == null)
			this.gatt = gatt;

		String method = "configureSensors";
		logInfo(method, "configuring sensors...");

		Map<String, String> charMap = gatt.getCharacteristicMap();
		charMap.keySet().forEach(uuid -> addHandleToChar(charMap.get(uuid), uuid));
		charMap.keySet().forEach(uuid -> {
			List<BleSensorAbstract<?, ?>> sensors = blueSt.getSensors(uuid);
			if ((sensors.size() > 0) && (!uuid.equals(BlueST.UUID_FIRMWARE_UPGRADE))) {
				BleSensorAbstract<?, ?> sensor = sensors.stream().filter(s -> s.getProperties().isEnabled()).findFirst()
				        .orElse(null);
				if (sensor != null) {
					logInfo(method, "Enabling notification for uuid : %s", uuid);
					gatt.enableNotification(uuid, GattConstants.ENABLE_NOTIFICATION);
				}
			}
		});

		sensorMap.forEach((feature, bleSensor) -> bleSensor.setBluetoothGatt(gatt));

		if (blueSt.getController() != null)
			blueSt.getController().setBluetoothGatt(gatt);

		if (blueSt.getFirmwareUpgrader() != null)
			blueSt.getFirmwareUpgrader().setBluetoothGatt(gatt);

		logInfo(method, "sensors configured");
	}
}
