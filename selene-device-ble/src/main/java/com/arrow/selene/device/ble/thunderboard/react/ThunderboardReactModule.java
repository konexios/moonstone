package com.arrow.selene.device.ble.thunderboard.react;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.arrow.selene.device.ble.BleModuleAbstract;
import com.arrow.selene.device.ble.thunderboard.ThunderboardData;
import com.arrow.selene.device.ble.thunderboard.ThunderboardProperties;
import com.arrow.selene.device.ble.thunderboard.react.handlers.LedsStateChangeHandler;
import com.arrow.selene.device.ble.thunderboard.react.sensor.AccelerationSensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.HumiditySensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.KeysSensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.LedsControl;
import com.arrow.selene.device.ble.thunderboard.react.sensor.LightSensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.OrientationSensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.TemperatureSensor;
import com.arrow.selene.device.ble.thunderboard.react.sensor.UvSensor;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.engine.DeviceData;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateUpdate;

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
