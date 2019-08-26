package moonstone.selene.device.xbee.zcl.domain.protocol.generic.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class GenericTunnelClusterAttributes {
	public static final int MAX_INCOMING_TRANSFER_SIZE_ATTRIBUTE_ID = 0x0000;
	public static final int MAX_OUTGOING_TRANSFER_SIZE_ATTRIBUTE_ID = 0x0001;
	public static final int PROTOCOL_ADDRESS_ATTRIBUTE_ID = 0x0002;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MAX_INCOMING_TRANSFER_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Maximum Incoming Transfer Size", null));
		ALL.put(MAX_OUTGOING_TRANSFER_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Maximum Outgoing Transfer Size", null));
		ALL.put(PROTOCOL_ADDRESS_ATTRIBUTE_ID, new ImmutablePair<>("Protocol Address", null));
	}
}
