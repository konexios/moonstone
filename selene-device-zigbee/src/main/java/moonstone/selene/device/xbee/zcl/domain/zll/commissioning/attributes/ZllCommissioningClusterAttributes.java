package moonstone.selene.device.xbee.zcl.domain.zll.commissioning.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class ZllCommissioningClusterAttributes {
	public static final int NUMBER_OF_RESETS_ATTRIBUTE_ID = 0x0000;
	public static final int PERSISTENT_MEMORY_WRITES_ATTRIBUTE_ID = 0x0001;
	public static final int MAC_RX_BCAST_ATTRIBUTE_ID = 0x0100;
	public static final int MAC_TX_BCAST_ATTRIBUTE_ID = 0x0101;
	public static final int MAC_RX_UCAST_ATTRIBUTE_ID = 0x0102;
	public static final int MAC_TX_UCAST_ATTRIBUTE_ID = 0x0103;
	public static final int MAC_TX_UCAST_RETRY_ATTRIBUTE_ID = 0x0104;
	public static final int MAC_TX_UCAST_FAIL_ATTRIBUTE_ID = 0x0105;
	public static final int APS_RX_BCAST_ATTRIBUTE_ID = 0x0106;
	public static final int APS_TX_BCAST_ATTRIBUTE_ID = 0x0107;
	public static final int APS_RX_UCAST_ATTRIBUTE_ID = 0x0108;
	public static final int APS_UCAST_SUCCESS_ATTRIBUTE_ID = 0x0109;
	public static final int APS_TX_UCAST_RETRY_ATTRIBUTE_ID = 0x010A;
	public static final int APS_TX_UCAST_FAIL_ATTRIBUTE_ID = 0x010B;
	public static final int ROUTE_DISC_INITIATED_ATTRIBUTE_ID = 0x010C;
	public static final int NEIGHBOR_ADDED_ATTRIBUTE_ID = 0x010D;
	public static final int NEIGHBOR_REMOVED_ATTRIBUTE_ID = 0x010E;
	public static final int NEIGHBOR_STALE_ATTRIBUTE_ID = 0x010F;
	public static final int JOIN_INDICATION_ATTRIBUTE_ID = 0x0110;
	public static final int CHILD_MOVED_ATTRIBUTE_ID = 0x0111;
	public static final int NWK_FC_FAILURE_ATTRIBUTE_ID = 0x0112;
	public static final int APS_FC_FAILURE_ATTRIBUTE_ID = 0x0113;
	public static final int APS_UNAUTHORIZED_KEY_ATTRIBUTE_ID = 0x0114;
	public static final int NWK_DECRYPT_FAILURE_ATTRIBUTE_ID = 0x0115;
	public static final int APS_DECRYPT_FAILURE_ATTRIBUTE_ID = 0x0116;
	public static final int PACKET_BUFFER_ALLOC_FAILURES_ATTRIBUTE_ID = 0x0117;
	public static final int RELAYED_UNICAST_ATTRIBUTE_ID = 0x0118;
	public static final int PHY_TO_MAC_QUEUE_LIMIT_REACHED_ATTRIBUTE_ID = 0x0119;
	public static final int PACKET_VALIDATE_DROP_COUNT_ATTRIBUTE_ID = 0x011A;
	public static final int AVERAGE_MAC_RETRY_PER_APS_MSG_SENT_ATTRIBUTE_ID = 0x011B;
	public static final int LAST_MESSAGE_LQI_ATTRIBUTE_ID = 0x011C;
	public static final int LAST_MESSAGE_RSSI_ATTRIBUTE_ID = 0x011D;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(NUMBER_OF_RESETS_ATTRIBUTE_ID, new ImmutablePair<>("Number Of Resets", null));
		ALL.put(PERSISTENT_MEMORY_WRITES_ATTRIBUTE_ID, new ImmutablePair<>("Persistent Memory Writes", null));
		ALL.put(MAC_RX_BCAST_ATTRIBUTE_ID, new ImmutablePair<>("MAC RX Broadcast", null));
		ALL.put(MAC_TX_BCAST_ATTRIBUTE_ID, new ImmutablePair<>("MAC TX Broadcast", null));
		ALL.put(MAC_RX_UCAST_ATTRIBUTE_ID, new ImmutablePair<>("MAC RX Unicast", null));
		ALL.put(MAC_TX_UCAST_ATTRIBUTE_ID, new ImmutablePair<>("MAC TX Unicast", null));
		ALL.put(MAC_TX_UCAST_RETRY_ATTRIBUTE_ID, new ImmutablePair<>("MAC TX Unicast Retry", null));
		ALL.put(MAC_TX_UCAST_FAIL_ATTRIBUTE_ID, new ImmutablePair<>("MAC TX Unicast Fail", null));
		ALL.put(APS_RX_BCAST_ATTRIBUTE_ID, new ImmutablePair<>("APS RX Broadcast", null));
		ALL.put(APS_TX_BCAST_ATTRIBUTE_ID, new ImmutablePair<>("APS TX Broadcast", null));
		ALL.put(APS_RX_UCAST_ATTRIBUTE_ID, new ImmutablePair<>("APS RX Unicast", null));
		ALL.put(APS_UCAST_SUCCESS_ATTRIBUTE_ID, new ImmutablePair<>("APS Unicast Success", null));
		ALL.put(APS_TX_UCAST_RETRY_ATTRIBUTE_ID, new ImmutablePair<>("APS TX Unicast Retry", null));
		ALL.put(APS_TX_UCAST_FAIL_ATTRIBUTE_ID, new ImmutablePair<>("APS TX Unicast Fail", null));
		ALL.put(ROUTE_DISC_INITIATED_ATTRIBUTE_ID, new ImmutablePair<>("Route Discovery Initiated", null));
		ALL.put(NEIGHBOR_ADDED_ATTRIBUTE_ID, new ImmutablePair<>("Neighbor Added", null));
		ALL.put(NEIGHBOR_REMOVED_ATTRIBUTE_ID, new ImmutablePair<>("Neighbor Removed", null));
		ALL.put(NEIGHBOR_STALE_ATTRIBUTE_ID, new ImmutablePair<>("Neighbor Stale", null));
		ALL.put(JOIN_INDICATION_ATTRIBUTE_ID, new ImmutablePair<>("Join Indication", null));
		ALL.put(CHILD_MOVED_ATTRIBUTE_ID, new ImmutablePair<>("Child Moved", null));
		ALL.put(NWK_FC_FAILURE_ATTRIBUTE_ID, new ImmutablePair<>("NWK FC Failure", null));
		ALL.put(APS_FC_FAILURE_ATTRIBUTE_ID, new ImmutablePair<>("APS FC Failure", null));
		ALL.put(APS_UNAUTHORIZED_KEY_ATTRIBUTE_ID, new ImmutablePair<>("APS Unauthorized Key", null));
		ALL.put(NWK_DECRYPT_FAILURE_ATTRIBUTE_ID, new ImmutablePair<>("NWK Decrypt Failure", null));
		ALL.put(APS_DECRYPT_FAILURE_ATTRIBUTE_ID, new ImmutablePair<>("APS Decrypt Failure", null));
		ALL.put(PACKET_BUFFER_ALLOC_FAILURES_ATTRIBUTE_ID,
				new ImmutablePair<>("Packet Buffer Allocation Failures", null));
		ALL.put(RELAYED_UNICAST_ATTRIBUTE_ID, new ImmutablePair<>("Relayed Unicast", null));
		ALL.put(PHY_TO_MAC_QUEUE_LIMIT_REACHED_ATTRIBUTE_ID,
				new ImmutablePair<>("PHY To MAC Queue Limit Reached", null));
		ALL.put(PACKET_VALIDATE_DROP_COUNT_ATTRIBUTE_ID, new ImmutablePair<>("Packet Validate Drop Count", null));
		ALL.put(AVERAGE_MAC_RETRY_PER_APS_MSG_SENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Average MAC Retry Per APS Message Sent", null));
		ALL.put(LAST_MESSAGE_LQI_ATTRIBUTE_ID, new ImmutablePair<>("Last Message LQI", null));
		ALL.put(LAST_MESSAGE_RSSI_ATTRIBUTE_ID, new ImmutablePair<>("Last Message RSSI", null));
	}
}
