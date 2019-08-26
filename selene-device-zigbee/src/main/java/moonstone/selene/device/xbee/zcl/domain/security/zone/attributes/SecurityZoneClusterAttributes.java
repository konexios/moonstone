package moonstone.selene.device.xbee.zcl.domain.security.zone.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class SecurityZoneClusterAttributes {
	public static final int ZONE_STATE_ATTRIBUTE_ID = 0x0000;
	public static final int ZONE_TYPE_ATTRIBUTE_ID = 0x0001;
	public static final int ZONE_STATUS_ATTRIBUTE_ID = 0x0002;

	public static final int IAS_CIE_ADDRESS_ATTRIBUTE_ID = 0x0010;
	public static final int ZONE_ID_ATTRIBUTE_ID = 0x0011;
	public static final int NUMBER_OF_ZONE_SENSITIVITY_LEVELS_SUPPORTED_ATTRIBUTE_ID = 0x0012;
	public static final int CURRENT_ZONE_SENSITIVITY_LEVEL_ATTRIBUTE_ID = 0x0013;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ZONE_STATE_ATTRIBUTE_ID, new ImmutablePair<>("Zone State", ZoneState.RESERVED));
		ALL.put(ZONE_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Zone Type", ZoneType.RESERVED_0X0029));
		ALL.put(ZONE_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Zone Status", ZoneStatus.OK));
		ALL.put(IAS_CIE_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("IAS SIE Address", null));
		ALL.put(ZONE_ID_ATTRIBUTE_ID, new ImmutablePair<>("Zone Id", null));
		ALL.put(NUMBER_OF_ZONE_SENSITIVITY_LEVELS_SUPPORTED_ATTRIBUTE_ID,
				new ImmutablePair<>("Number Of Zone Sensitivity Levels Supported", null));
		ALL.put(CURRENT_ZONE_SENSITIVITY_LEVEL_ATTRIBUTE_ID,
				new ImmutablePair<>("Current Zone Sensitivity Level", null));
	}
}
