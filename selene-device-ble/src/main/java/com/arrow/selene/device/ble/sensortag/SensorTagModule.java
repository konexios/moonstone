package com.arrow.selene.device.ble.sensortag;

import java.util.Properties;

import com.arrow.selene.device.ble.BleModuleAbstract;
import com.arrow.selene.device.ble.sensortag.sensor.HumiditySensor;
import com.arrow.selene.device.ble.sensortag.sensor.KeysSensor;
import com.arrow.selene.device.ble.sensortag.sensor.LightSensor;
import com.arrow.selene.device.ble.sensortag.sensor.MovementSensor;
import com.arrow.selene.device.ble.sensortag.sensor.PressureSensor;
import com.arrow.selene.device.ble.sensortag.sensor.TemperatureSensor;

public class SensorTagModule
        extends BleModuleAbstract<SensorTagInfo, SensorTagProperties, SensorTagStates, SensorTagData> {

	public SensorTagModule() {

	}

	@Override
	public void init(Properties props) {
		initSensor(props, new MovementSensor());
		initSensor(props, new PressureSensor());
		initSensor(props, new HumiditySensor());
		initSensor(props, new KeysSensor());
		initSensor(props, new LightSensor());
		initSensor(props, new TemperatureSensor());

		super.init(props);
	}

	@Override
	protected SensorTagProperties createProperties() {
		return new SensorTagProperties();
	}

	@Override
	protected SensorTagInfo createInfo() {
		return new SensorTagInfo();
	}

	@Override
	protected SensorTagStates createStates() {
		return new SensorTagStates();
	}
}
