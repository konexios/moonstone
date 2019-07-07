package com.arrow.selene.device.xbee.zcl.domain.general.partition.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class PartitionClusterAttributes {
	public static final int PARTITION_MAXIMUM_INCOMING_TRANSFER_SIZE_ATTRIBUTE_ID = 0x0000;
	public static final int PARTITION_MAXIMUM_OUTGOING_TRANSFER_SIZE_ATTRIBUTE_ID = 0x0001;
	public static final int PARTIONED_FRAME_SIZE_ATTRIBUTE_ID = 0x0002;
	public static final int LARGE_FRAME_SIZE_ATTRIBUTE_ID = 0x0003;
	public static final int NUMBER_OF_ACK_FRAME_ATTRIBUTE_ID = 0x0004;
	public static final int NACK_TIMEOUT_ATTRIBUTE_ID = 0x0005;
	public static final int INTERFRAME_DELAY_ATTRIBUTE_ID = 0x0006;
	public static final int NUMBER_OF_SEND_RETRIES_ATTRIBUTE_ID = 0x0007;
	public static final int SENDER_TIMEOUT_ATTRIBUTE_ID = 0x0008;
	public static final int RECEIVER_TIMEOUT_ATTRIBUTE_ID = 0x0009;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(PARTITION_MAXIMUM_INCOMING_TRANSFER_SIZE_ATTRIBUTE_ID,
				new ImmutablePair<>("Partition Maximum Incoming Transfer Size", null));
		ALL.put(PARTITION_MAXIMUM_OUTGOING_TRANSFER_SIZE_ATTRIBUTE_ID,
				new ImmutablePair<>("Partition Maximum Outgoing Transfer Size", null));
		ALL.put(PARTIONED_FRAME_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Partioned Frame Size", null));
		ALL.put(LARGE_FRAME_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Large Frame Size", null));
		ALL.put(NUMBER_OF_ACK_FRAME_ATTRIBUTE_ID, new ImmutablePair<>("Number Of Ack Frame", null));
		ALL.put(NACK_TIMEOUT_ATTRIBUTE_ID, new ImmutablePair<>("Nack Timeout", null));
		ALL.put(INTERFRAME_DELAY_ATTRIBUTE_ID, new ImmutablePair<>("Interframe Delay", null));
		ALL.put(NUMBER_OF_SEND_RETRIES_ATTRIBUTE_ID, new ImmutablePair<>("Number Of Send Retries", null));
		ALL.put(SENDER_TIMEOUT_ATTRIBUTE_ID, new ImmutablePair<>("Sender Timeout", null));
		ALL.put(RECEIVER_TIMEOUT_ATTRIBUTE_ID, new ImmutablePair<>("Receiver Timeout", null));
	}
}
