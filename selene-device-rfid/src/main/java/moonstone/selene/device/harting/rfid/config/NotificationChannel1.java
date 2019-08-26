package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SerializationUtils;

public class NotificationChannel1 implements ConfigParameter<NotificationChannel1> {
	private static final long serialVersionUID = 2809949289575073688L;

	private static final int ID = 49;

	private static final Pattern DOT = Pattern.compile("\\.");

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_ENABLE_ACKNOWLEDGE =
			"OperatingMode_NotificationMode_Transmission_Enable_Acknowledge";
	private boolean acknowledge;

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_ENABLE =
			"OperatingMode_NotificationMode_Transmission_KeepAlive_Enable";
	private boolean keepAliveEnabled;

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_INTERVAL_TIME =
			"OperatingMode_NotificationMode_Transmission_KeepAlive_IntervalTime";
	private int keepAliveTime;

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_IPV4_IP_ADDRESS =
			"OperatingMode_NotificationMode_Transmission_Destination_IPv4_IPAddress";
	private byte[] destinationIpAddress;

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_PORT_NUMBER =
			"OperatingMode_NotificationMode_Transmission_Destination_PortNumber";
	private int destinationIpPort;

	private static final String OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_CONNECTION_HOLD_TIME =
			"OperatingMode_NotificationMode_Transmission_Destination_ConnectionHoldTime";
	private int holdTime;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_ENABLE_ACKNOWLEDGE);
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_ENABLE);
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_INTERVAL_TIME);
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_IPV4_IP_ADDRESS);
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_PORT_NUMBER);
		ALL.add(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_CONNECTION_HOLD_TIME);
	}

	public NotificationChannel1 withAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
		return this;
	}

	public NotificationChannel1 withKeepAliveEnabled(boolean keepAliveEnabled) {
		this.keepAliveEnabled = keepAliveEnabled;
		return this;
	}

	public NotificationChannel1 withKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
		return this;
	}

	public NotificationChannel1 withDestinationIpAddress(byte[] destinationIpAddress) {
		this.destinationIpAddress = destinationIpAddress;
		return this;
	}

	public NotificationChannel1 withDestinationIpPort(int destinationIpPort) {
		this.destinationIpPort = destinationIpPort;
		return this;
	}

	public NotificationChannel1 withHoldTime(int holdTime) {
		this.holdTime = holdTime;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) (acknowledge ? 0b10000000 : 0));
		buffer.put(4, (byte) (keepAliveEnabled ? 1 : 0));
		buffer.putShort(5, (short) keepAliveTime);
		buffer.put(destinationIpAddress);
		buffer.putShort(11, (short) destinationIpPort);
		buffer.put(13, (byte) holdTime);
		return buffer.array();
	}

	@Override
	public NotificationChannel1 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		boolean acknowledge = (buffer.get(0) & 0b10000000) != 0;
		boolean keepAliveEnabled = buffer.get(4) == 1;
		int keepAliveTime = Short.toUnsignedInt(buffer.getShort(5));
		byte[] destinationIpAddress = new byte[4];
		buffer.get(destinationIpAddress);
		int destinationIpPort = Short.toUnsignedInt(buffer.getShort(11));
		int holdTime = Byte.toUnsignedInt(buffer.get(13));
		return new NotificationChannel1().withAcknowledge(acknowledge).withKeepAliveEnabled(keepAliveEnabled)
				.withKeepAliveTime(keepAliveTime).withDestinationIpAddress(destinationIpAddress).withDestinationIpPort(
						destinationIpPort).withHoldTime(holdTime);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public boolean updateState(String name, String value) {
		NotificationChannel1 stored = SerializationUtils.clone(this);
		switch (name) {
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_ENABLE_ACKNOWLEDGE: {
				acknowledge = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_ENABLE: {
				keepAliveEnabled = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_INTERVAL_TIME: {
				keepAliveTime = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_IPV4_IP_ADDRESS: {
				String[] values = DOT.split(value);
				for (int i = 0; i < 4; i++) {
					destinationIpAddress[i] = (byte) Integer.parseInt(values[i]);
				}
				break;
			}
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_PORT_NUMBER: {
				destinationIpPort = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_CONNECTION_HOLD_TIME: {
				holdTime = Integer.parseInt(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_ENABLE_ACKNOWLEDGE, Boolean.toString(acknowledge));
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_ENABLE, Boolean.toString
				(keepAliveEnabled));
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_KEEP_ALIVE_INTERVAL_TIME,
				Integer.toString(keepAliveTime));
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_IPV4_IP_ADDRESS,
				Byte.toUnsignedInt(destinationIpAddress[0]) + "." + Byte.toUnsignedInt(destinationIpAddress[1]) + '.' +
						Byte.toUnsignedInt(destinationIpAddress[2]) + '.' +
						Byte.toUnsignedInt(destinationIpAddress[3]));
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_PORT_NUMBER,
				Integer.toString(destinationIpPort));
		result.put(OPERATING_MODE_NOTIFICATION_MODE_TRANSMISSION_DESTINATION_CONNECTION_HOLD_TIME,
				Integer.toString(holdTime));
		return result;
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		NotificationChannel1 that = (NotificationChannel1) o;
		return acknowledge == that.acknowledge && keepAliveEnabled == that.keepAliveEnabled &&
				keepAliveTime == that.keepAliveTime && destinationIpPort == that.destinationIpPort &&
				holdTime == that.holdTime && Arrays.equals(destinationIpAddress, that.destinationIpAddress);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(acknowledge, keepAliveEnabled, keepAliveTime, destinationIpPort, holdTime);
		result = 31 * result + Arrays.hashCode(destinationIpAddress);
		return result;
	}
}
