package com.arrow.selene.device.ble.sensortile;

import java.util.List;
import java.util.Map;

import com.arrow.selene.device.ble.gatt.Gatt;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.specs.BlueST;
import com.arrow.selene.device.ble.specs.bluest.AbstractBlueSTModule;

public class SensorTileModule
        extends AbstractBlueSTModule<SensorTileInfo, SensorTileProperties, SensorTileStates, SensorTileData> {

	@Override
	protected SensorTileProperties createProperties() {
		return new SensorTileProperties();
	}

	@Override
	protected SensorTileInfo createInfo() {
		return new SensorTileInfo();
	}

	@Override
	protected SensorTileStates createStates() {
		return new SensorTileStates();
	}

	@Override
	protected void configureSensors(Gatt gatt) {
		if (this.gatt == null)
			this.gatt = gatt;

		String method = "SensorTile.configureSensors";
		logInfo(method, "configuring sensors...");

		Map<String, String> charMap = gatt.getCharacteristicMap();
		charMap.keySet().forEach(uuid -> addHandleToChar(charMap.get(uuid), uuid));
		charMap.keySet().forEach(uuid -> {
			List<BleSensorAbstract<?, ?>> sensors = blueSt.getSensors(uuid);
			if ((sensors.size() > 0) && (uuid != BlueST.UUID_FIRMWARE_UPGRADE)) {
				BleSensorAbstract<?, ?> sensor = sensors.stream().filter(s -> s.getProperties().isEnabled()).findAny()
				        .orElse(null);
				if (sensor != null) {
					logInfo(method, "Enabling notification for uuid : %s", uuid);
					gatt.enableNotification(uuid, SensorTileGattConstants.ENABLE_NOTIFICATION);
				}
			}
		});

		sensorMap.forEach((feature, bleSensor) -> bleSensor.configure(gatt));
		logInfo(method, "sensors configured");
	}
}
