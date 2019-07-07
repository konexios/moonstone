package com.arrow.selene.device.ble.specs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.ble.sensor.BleSensorAbstract;

public class BlueST {

	public static final String suffix = "-11e1-ac36-0002a5d5c51b";
	private static final String uuidSuffix = "-0001" + suffix;
	public static final String UUID_FEATURE_COMMAND = "00000002-000f" + suffix;
	public static final String UUID_FIRMWARE_UPGRADE = "00000001-000e" + suffix;

	private Map<Integer, BleSensorAbstract<?, ?>> sensorMap = new HashMap<>();
	BleSensorAbstract<?, ?> commandSensor, firmwareUpgradeSensor;

	public enum Feature {
		PEDOMETER(0), MEMS_GESTURE(1), PROXIMITY_GESTURE(2), CARRY_POSITION(3), ACTIVITY(4), COMPASS(
		        5), MOTION_INTENSITY(6), SENSOR_FUSION(7), SENSOR_FUSION_COMPACT(8), FREE_FALL(9), ACCELERATION_EVENT(
		                10), BEAM_FORMING(11), SD_LOGGING(12), CO_SENSOR(15), TEMPERATURE_SECOND(16), BATTERY(
		                        17), TEMPERATURE(18), HUMIDITY(19), PRESSURE(20), MAGNETOMETER(21), GYROMETER(
		                                22), ACCELEROMETER(23), LIGHT(24), PROXIMITY(25), MIC_LEVEL(26), AUDIO_ADPC(
		                                        27), DIRECTION_OF_ARRIVAL(28), SWITCH(29), AUDIO_ADPCM_SYNC(30);

		private int bit;

		private Feature(int bit) {
			this.bit = bit;
		}

		public int getBit() {
			return bit;
		}
	}

	public static byte[] longToHex(final long l) {
		long v = l & 0xFFFFFFFFFFFFFFFFL;
		byte[] result = new byte[16];
		Arrays.fill(result, 0, result.length, (byte) 0);
		for (int i = 0; i < result.length; i += 2) {
			byte b = (byte) ((v & 0xFF00000000000000L) >> 56);
			byte b2 = (byte) (b & 0x0F);
			byte b1 = (byte) ((b >> 4) & 0x0F);
			if (b1 > 9)
				b1 += 39;
			b1 += 48;
			if (b2 > 9)
				b2 += 39;
			b2 += 48;
			result[i] = (byte) (b1 & 0xFF);
			result[i + 1] = (byte) (b2 & 0xFF);
			v <<= 8;
		}
		return result;
	}

	public static String getUuid(Feature feature) {
		long mask = (long) Math.pow(2, feature.getBit());
		String uuid = String.format("%08x", mask) + uuidSuffix;
		return uuid;
	}

	public void setSensor(Feature feature, BleSensorAbstract<?, ?> sensor) {
		sensorMap.put(feature.getBit(), sensor);
	}

	public void setController(BleSensorAbstract<?, ?> sensor) {
		commandSensor = sensor;
	}

	public void setFirmwareUpgrader(BleSensorAbstract<?, ?> sensor) {
		firmwareUpgradeSensor = sensor;
	}

	public BleSensorAbstract<?, ?> getController() {
		return commandSensor;
	}

	public BleSensorAbstract<?, ?> getFirmwareUpgrader() {
		return firmwareUpgradeSensor;
	}

	public List<BleSensorAbstract<?, ?>> getSensors(String uuid) {
		if (!uuid.contains(suffix))
			return Collections.emptyList();

		if (uuid.equals(UUID_FEATURE_COMMAND)) {
			if (commandSensor != null)
				return Collections.singletonList(commandSensor);
			return Collections.emptyList();
		}

		if (uuid.equals(UUID_FIRMWARE_UPGRADE)) {
			if (firmwareUpgradeSensor != null)
				return Collections.singletonList(firmwareUpgradeSensor);
			return Collections.emptyList();
		}

		if (!uuid.contains(uuidSuffix))
			return Collections.emptyList();

		List<BleSensorAbstract<?, ?>> sensors = new ArrayList<>();
		Integer featureMask = Integer.parseInt(uuid.substring(0, 8), 16);

		for (int bit = 30; bit >= 0; bit--) {
			int feature = (featureMask >> bit) & 0x01;
			if (feature > 0) {
				BleSensorAbstract<?, ?> sensor = sensorMap.get(bit);
				if (sensor != null) {
					sensors.add(sensor);
				}
			}
		}
		return sensors;
	}
}
