package com.arrow.selene.device.ble.thunderboard.sense;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.arrow.selene.device.ble.BleModuleAbstract;
import com.arrow.selene.device.ble.thunderboard.ThunderboardData;
import com.arrow.selene.device.ble.thunderboard.ThunderboardProperties;
import com.arrow.selene.device.ble.thunderboard.sense.handlers.LedsStateChangeHandler;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.AccelerationSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.CO2Sensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.HumiditySensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.KeysSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.LightSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.OrientationSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.PressureSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.RgbLedsControl;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.SoundSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.TemperatureSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.TvocSensor;
import com.arrow.selene.device.ble.thunderboard.sense.sensor.UvSensor;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.engine.DeviceData;
import com.arrow.selene.engine.state.StateUpdate;

public class ThunderboardSenseModule extends
        BleModuleAbstract<ThunderboardSenseInfo, ThunderboardProperties, ThunderboardSenseStates, ThunderboardData> {
	private final RgbLedsControl rgbLedsControl;
	private final LedsStateChangeHandler ledsStateChangeHandler;

	public ThunderboardSenseModule() {
		rgbLedsControl = new RgbLedsControl();
		ledsStateChangeHandler = new LedsStateChangeHandler(rgbLedsControl);
		handlers = Collections.singletonList(ledsStateChangeHandler);
	}

	@Override
	public void init(Properties props) {
		initSensor(props, new AccelerationSensor());
		initSensor(props, new OrientationSensor());
		initSensor(props, new UvSensor());
		initSensor(props, new PressureSensor());
		initSensor(props, new TemperatureSensor());
		initSensor(props, new HumiditySensor());
		initSensor(props, new LightSensor());
		initSensor(props, new SoundSensor());
		initSensor(props, new CO2Sensor());
		initSensor(props, new TvocSensor());
		initSensor(props, new KeysSensor());
		initSensor(props, rgbLedsControl);
		super.init(props);
	}

	@Override
	protected ThunderboardProperties createProperties() {
		return new ThunderboardProperties();
	}

	@Override
	protected ThunderboardSenseInfo createInfo() {
		return new ThunderboardSenseInfo();
	}

	@Override
	protected ThunderboardSenseStates createStates() {
		return new ThunderboardSenseStates();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void queueDataForSending(DeviceData data, boolean processSequential) {
		
		System.out.println("\n ThunderboardSenseMobile.queueDataForSending()");
		
		super.queueDataForSending(data, processSequential);

		SensorDataImpl<? extends SensorData<?>> sensorData = (SensorDataImpl<? extends SensorData<?>>) data;
		int color = 0;
		int leds = 0;
		boolean hasStates = false;
		for (SensorData<?> sd : sensorData.getSensorDataList()) {
			if (Objects.equals(sd.getName(), RgbLedsControl.LEDS)) {
				leds = ((IntegerSensorData) sd).getData();
				hasStates = true;
			} else if (Objects.equals(sd.getName(), RgbLedsControl.COLOR)) {
				color = ((IntegerSensorData) sd).getData();
				hasStates = true;
			}
		}
		if (hasStates) {
			Map<String, String> updatedStates = getStates()
			        .importStates(ThunderboardSenseStates.extractStates(color, leds));
			if (!updatedStates.isEmpty()) {
				queueStatesForSending(new StateUpdate().withStates(updatedStates));
			}
		}
	}
}
