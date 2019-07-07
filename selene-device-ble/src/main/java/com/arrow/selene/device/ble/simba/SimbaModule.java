package com.arrow.selene.device.ble.simba;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.arrow.selene.SeleneException;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.simba.handlers.LedStateChangeHandler;
import com.arrow.selene.device.ble.simba.sensor.LedControl;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.bluest.AbstractBlueSTModule;
import com.arrow.selene.device.ble.specs.bluest.sensor.FirmwareUpgrader;
import com.arrow.selene.device.sensor.BooleanSensorData;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.engine.DeviceData;
import com.arrow.selene.engine.state.StateUpdate;

public class SimbaModule extends AbstractBlueSTModule<SimbaInfo, SimbaProperties, SimbaStates, SimbaData> {

	private final FirmwareUpgrader firmwareUpgrader;
	private final LedControl ledControl;
	private final LedStateChangeHandler ledStateChangeHandler;

	public SimbaModule() {
		firmwareUpgrader = new FirmwareUpgrader();
		ledControl = new LedControl();
		ledStateChangeHandler = new LedStateChangeHandler(ledControl);
		handlers = Collections.singletonList(ledStateChangeHandler);

		blueSt.setController(ledControl);
		blueSt.setFirmwareUpgrader(firmwareUpgrader);
	}

	@Override
	public void init(Properties props) {
		initSensor(props, ledControl);
		initSensor(props, firmwareUpgrader);
		super.init(props);
	}

	@Override
	protected SimbaProperties createProperties() {
		return new SimbaProperties();
	}

	@Override
	protected SimbaInfo createInfo() {
		return new SimbaInfo();
	}

	@Override
	protected SimbaStates createStates() {
		return new SimbaStates();
	}

	@Override
	public boolean isRandomAddress() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void queueDataForSending(DeviceData data, boolean processSequential) {
		super.queueDataForSending(data, processSequential);

		SensorDataImpl<? extends SensorData<?>> sensorData = (SensorDataImpl<? extends SensorData<?>>) data;

		sensorData.getSensorDataList().stream().filter(sensor -> Objects.equals(sensor.getName(), LedControl.LED))
		        .forEach(sensor -> {
			        boolean led = false;
			        led = ((BooleanSensorData) sensor).getData();
			        Map<String, String> updatedStates = getStates().importStates(SimbaStates.extractStates(led));
			        if (!updatedStates.isEmpty()) {
				        queueStatesForSending(new StateUpdate().withStates(updatedStates));
			        }
		        });
	}

	@Override
	protected void doFirmwareUpgrade(byte[] bytes) {
		firmwareUpgrader.onFirmwareCharacteristicsChanged(bytes);
	}

	@Override
	public void upgradeDeviceSoftware(File file) throws SeleneException {
		String method = "upgradeDeviceSoftware";
		logInfo(method, "got file to upgrade");

		// Disable Sensors For FW Upgrade
		gatt.getCharacteristicMap()
		        .forEach((uuid, handle) -> gatt.disableNotification(uuid, GattConstants.DISABLE_NOTIFICATION));

		firmwareUpgrader.enable();
		firmwareUpgrader.upgradeFwFromLoadedFile(file);

		logInfo(method, "wait till upgrade is done ...");
		try {
			firmwareUpgrader.waitForUpgrade();
		} finally {

			// Enable Sensors After FW Upgrade
			gatt.getCharacteristicMap().keySet().forEach(uuid -> {
				List<BleSensorAbstract<?, ?>> sensors = blueSt.getSensors(uuid);
				if (!sensors.isEmpty() && !Objects.equals(uuid, BlueST.UUID_FIRMWARE_UPGRADE)) {
					BleSensorAbstract<?, ?> sensor = sensors.stream().filter(s -> s.getProperties().isEnabled())
					        .findFirst().orElse(null);
					if (sensor != null) {
						logInfo(method, "Enabling notification for uuid : %s", uuid);
						gatt.enableNotification(uuid, GattConstants.ENABLE_NOTIFICATION);
					}
				}
			});
			firmwareUpgrader.disable();
		}
	}
}
