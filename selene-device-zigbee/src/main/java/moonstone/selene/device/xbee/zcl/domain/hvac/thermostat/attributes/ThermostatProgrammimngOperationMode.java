package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum ThermostatProgrammimngOperationMode implements Attribute<SetSensorData> {
	SIMPLE_SETPOINT_MODE((byte) 0, false),
	SCHEDULE_PROGRAMMING_MODE((byte) 0, true),
	AUTO_RECOVERY_MODE_OFF((byte) 1, false),
	AUTO_RECOVERY_MODE_ON((byte) 1, true),
	ECONOMY_ENERGY_STAR_MODE_OFF((byte) 2, false),
	ECONOMY_ENERGY_STAR_MODE_ON((byte) 2, true);

	private final byte bit;
	private final boolean value;

	ThermostatProgrammimngOperationMode(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<ThermostatProgrammimngOperationMode> getByValue(byte... value) {
		Set<ThermostatProgrammimngOperationMode> result = EnumSet.noneOf(ThermostatProgrammimngOperationMode.class);
		for (ThermostatProgrammimngOperationMode item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.THERMOSTAT_PROGRAMMING_OPERATION_MODE_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
