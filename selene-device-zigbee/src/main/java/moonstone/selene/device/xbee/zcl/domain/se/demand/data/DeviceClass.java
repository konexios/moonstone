package moonstone.selene.device.xbee.zcl.domain.se.demand.data;

import java.util.EnumSet;
import java.util.Set;

public enum DeviceClass {
	HVAC_COMPRESSOR_OR_FURNACE,
	STRIP_HEATERS_BASEBOARD_HEATERS,
	WATER_HEATER,
	POOL_PUMP_SPA_JACUZZI,
	SMART_APPLIANCES,
	IRRIGATION_PUMP,
	MANAGED_COMMERCIAL_AND_INDUSTRIAL,
	SIMPLE_MISC_RESIDENTIAL_ON_OFF_LOADS,
	EXTERIOR_LIGHTING,
	INTERIOR_LIGHTING,
	ELECTRIC_VEHICLE,
	GENERATION_SYSTEMS;

	public static Set<DeviceClass> getByValue(int value) {
		Set<DeviceClass> result = EnumSet.noneOf(DeviceClass.class);
		for (DeviceClass item : values()) {
			if ((value >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getByValues(Set<DeviceClass> items) {
		int result = 0;
		for (DeviceClass item : items) {
			result |= 1 << item.ordinal();
		}
		return result;
	}
}
