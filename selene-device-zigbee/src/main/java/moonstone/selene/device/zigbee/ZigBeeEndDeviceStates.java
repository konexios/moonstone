package moonstone.selene.device.zigbee;

import java.util.HashSet;
import java.util.Set;

import moonstone.selene.device.xbee.zcl.data.AttributeRecord;

public class ZigBeeEndDeviceStates extends ZigBeeStatesAbstract {
	private static final long serialVersionUID = 1124546193565427419L;

	private Set<AttributeRecord> states = new HashSet<>();

	public Set<AttributeRecord> getStates() {
		return states;
	}
}
