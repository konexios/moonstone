package moonstone.selene.device.ble.thunderboard.sense;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import moonstone.selene.device.ble.BleModuleAbstract;
import moonstone.selene.device.ble.thunderboard.ThunderboardData;
import moonstone.selene.device.ble.thunderboard.ThunderboardProperties;
import moonstone.selene.device.ble.thunderboard.sense.handlers.LedsStateChangeHandler;
import moonstone.selene.device.ble.thunderboard.sense.sensor.AccelerationSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.CO2Sensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.HumiditySensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.KeysSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.LightSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.OrientationSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.PressureSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.RgbLedsControl;
import moonstone.selene.device.ble.thunderboard.sense.sensor.SoundSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.TemperatureSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.TvocSensor;
import moonstone.selene.device.ble.thunderboard.sense.sensor.UvSensor;
import moonstone.selene.device.sensor.IntegerSensorData;
import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.sensor.SensorDataImpl;
import moonstone.selene.engine.DeviceData;
import moonstone.selene.engine.state.StateUpdate;

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
