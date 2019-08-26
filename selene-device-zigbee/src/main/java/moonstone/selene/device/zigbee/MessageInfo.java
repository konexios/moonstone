package moonstone.selene.device.zigbee;

import java.time.Instant;

public class MessageInfo {
	public final static int DEFAULT_NUM_RETRIES = 3;
	public final static long DEFAULT_RETRY_INTERVAL_SECS = 1200;

	private byte sequence;
	private MessageType type;
	private String address;
	private int endpoint;
	private int clusterId;
	private byte[] payload;
	private String description;

	private int expectedResponseId;
	private int numberOfRetries;
	private long lastRetry;

	public MessageInfo(byte sequence, MessageType type, String address, int endpoint, int clusterId, byte[] payload,
	        String description, int expectedResponseId) {
		this(sequence, type, address, endpoint, clusterId, payload, description, expectedResponseId,
		        DEFAULT_NUM_RETRIES);
	}

	public MessageInfo(byte sequence, MessageType type, String address, int endpoint, int clusterId, byte[] payload,
	        String description, int expectedResponseId, int numberOfRetries) {
		this.sequence = sequence;
		this.type = type;
		this.address = address;
		this.endpoint = endpoint;
		this.clusterId = clusterId;
		this.payload = payload;
		this.description = description;
		this.expectedResponseId = expectedResponseId;
		this.numberOfRetries = numberOfRetries;
	}

	public byte getSequence() {
		return sequence;
	}

	public MessageType getType() {
		return type;
	}

	public String getAddress() {
		return address;
	}

	public int getEndpoint() {
		return endpoint;
	}

	public int getClusterId() {
		return clusterId;
	}

	public byte[] getPayload() {
		return payload;
	}

	public String getDescription() {
		return description;
	}

	public int getExpectedResponseId() {
		return expectedResponseId;
	}

	public int getNumberOfRetries() {
		return numberOfRetries;
	}

	public int decrementRetries() {
		return --numberOfRetries;
	}

	public long getLastRetry() {
		return lastRetry;
	}

	public void tryNow() {
		lastRetry = Instant.now().getEpochSecond();
	}

	public boolean shouldWeRetry() {
		return Instant.now().getEpochSecond() - lastRetry > DEFAULT_RETRY_INTERVAL_SECS;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Byte.toUnsignedInt(sequence));
		sb.append('|');
		sb.append(type);
		sb.append('|');
		sb.append(address);
		sb.append('|');
		sb.append(endpoint);
		sb.append('|');
		sb.append(clusterId);
		sb.append('|');
		sb.append(expectedResponseId);
		sb.append('|');
		sb.append(description);
		return sb.toString();
	}
}
