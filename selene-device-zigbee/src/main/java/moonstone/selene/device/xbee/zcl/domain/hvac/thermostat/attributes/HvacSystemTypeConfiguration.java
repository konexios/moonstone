package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum HvacSystemTypeConfiguration implements Attribute<SetSensorData> {
	COOL_STAGE1(0, 2, 0),
	COOL_STAGE2(0, 2, 1),
	COOL_STAGE3(0, 2, 2),
	HEAT_STAGE1(2, 2, 0),
	HEAT_STAGE2(2, 2, 1),
	HEAT_STAGE3(2, 2, 2),
	HEATING_SYSTEM_CONVENTIONAL(4, 1, 0),
	HEATING_SYSTEM_HEAT_PUMP(4, 1, 1),
	HEATING_FUEL_ELECTRICAL(5, 1, 0),
	HEATING_FUEL_GAS(5, 1, 1);

	private final int startBit;
	private final int length;
	private final int value;

	HvacSystemTypeConfiguration(int startBit, int length, int value) {
		this.startBit = startBit;
		this.length = length;
		this.value = value;
	}

	public static Set<HvacSystemTypeConfiguration> getByValue(byte... value) {
		Set<HvacSystemTypeConfiguration> result = EnumSet.noneOf(HvacSystemTypeConfiguration.class);
		for (HvacSystemTypeConfiguration item : values()) {
			if ((value[0] >> item.startBit & (2 << item.length - 1) - 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.HVAC_SYSTEM_TYPE_CONFIGURATION_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
