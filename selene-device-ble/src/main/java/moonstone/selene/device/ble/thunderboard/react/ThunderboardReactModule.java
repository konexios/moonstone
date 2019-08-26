package moonstone.selene.device.ble.thunderboard.react;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import moonstone.selene.device.ble.BleModuleAbstract;
import moonstone.selene.device.ble.thunderboard.ThunderboardData;
import moonstone.selene.device.ble.thunderboard.ThunderboardProperties;
import moonstone.selene.device.ble.thunderboard.react.handlers.LedsStateChangeHandler;
import moonstone.selene.device.ble.thunderboard.react.sensor.AccelerationSensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.HumiditySensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.KeysSensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.LedsControl;
import moonstone.selene.device.ble.thunderboard.react.sensor.LightSensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.OrientationSensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.TemperatureSensor;
import moonstone.selene.device.ble.thunderboard.react.sensor.UvSensor;
import moonstone.selene.device.sensor.IntegerSensorData;
import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.sensor.SensorDataImpl;
import moonstone.selene.engine.DeviceData;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateUpdate;

public class ThunderboardReactModule extends
        BleModuleAbstract<ThunderboardReactInfo, ThunderboardProperties, ThunderboardReactStates, ThunderboardData> {
	private LedsControl ledsControl = new LedsControl();

	public ThunderboardReactModule() {
		handlers = Collections.singletonList(new LedsStateChangeHandler(ledsControl));
	}

	@Override
	public void init(Properties props) {
		initSensor(props, new AccelerationSensor());
		initSensor(props, new OrientationSensor());
		initSensor(props, new KeysSensor());
		initSensor(props, new HumiditySensor());
		initSensor(props, new TemperatureSensor());
		initSensor(props, new UvSensor());
		initSensor(props, new LightSensor());
		initSensor(props, ledsControl);
		super.init(props);
	}

	@Override
	protected ThunderboardProperties createProperties() {
		return new ThunderboardProperties();
	}

	@Override
	protected ThunderboardReactInfo createInfo() {
		return new ThunderboardReactInfo();
	}

	@Override
	protected ThunderboardReactStates createStates() {
		return new ThunderboardReactStates();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void queueDataForSending(DeviceData data, boolean processSequential) {
		super.queueDataForSending(data, processSequential);

		SensorDataImpl<? extends SensorData<?>> sensorData = (SensorDataImpl<? extends SensorData<?>>) data;
		int leds = 0;
		boolean hasStates = false;
		for (SensorData<?> sd : sensorData.getSensorDataList()) {
			if (Objects.equals(sd.getName(), LedsControl.LEDS)) {
				leds = ((IntegerSensorData) sd).getData();
				hasStates = true;
			}
		}
		if (hasStates) {
			Map<String, State> newStates = ThunderboardReactStates.extractStates(leds);
			Map<String, String> updatedStates = getStates().importStates(newStates);
			if (!updatedStates.isEmpty()) {
				queueStatesForSending(new StateUpdate().withStates(updatedStates));
			}
		}
	}
}
