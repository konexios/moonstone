package moonstone.selene.device.xbee.zcl.domain.protocol.iso7816.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class Iso7816ProtocolTunnelClusterAttributes {
	public static final int ISO7816_PROTOCOL_TUNNEL_STATUS_ATTRIBUTE_ID = 0x0000;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ISO7816_PROTOCOL_TUNNEL_STATUS_ATTRIBUTE_ID,
				new ImmutablePair<>("Iso7816 Protocol Tunnel Status", null));
	}
}
