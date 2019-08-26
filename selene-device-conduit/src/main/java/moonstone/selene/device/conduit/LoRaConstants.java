package moonstone.selene.device.conduit;

public interface LoRaConstants {
	public interface TopicType {
		final static String UP = "up";
		final static String DOWN = "down";
		final static String JOINED = "joined";
		final static String PACKET_RECV = "packet_recv";
		final static String PACKET_SENT = "packet_sent";
		final static String QUEUE_FULL = "queue_full";
	}

	public final static String DOWNLINK_TOPIC_FORMAT = "lora/%s/down";
}
