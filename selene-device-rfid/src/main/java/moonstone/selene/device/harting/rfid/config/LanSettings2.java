package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SerializationUtils;

public class LanSettings2 implements ConfigParameter<LanSettings2> {
	private static final long serialVersionUID = 5086161976059105208L;

	private static final int ID = 41;

	private static final String HOST_INTERFACE_INTERFACES_LAN_IPV4_SUBNET_MASK =
			"HostInterface_Interfaces_LAN_IPv4_SubnetMask";
	private static final Pattern DOT = Pattern.compile("\\.");
	private byte[] subnetMask;

	private static final String HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_ENABLE =
			"HostInterface_Interfaces_LAN_Keepalive_Enable";
	private static final String HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DISABLE =
			"HostInterface_Interfaces_LAN_Autonegotiation_Disable";
	private static final String HOST_INTERFACE_INTERFACES_LAN_HOSTNAME_ENABLE =
			"HostInterface_Interfaces_LAN_Hostname_Enable";
	private static final String HOST_INTERFACE_INTERFACES_LAN_IPV4_ENABLE_DHCP =
			"HostInterface_Interfaces_LAN_IPv4_Enable_DHCP";
	private static final String HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DUPLEX =
			"HostInterface_Interfaces_LAN_Autonegotiation_Duplex";
	private static final String HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_SPEED =
			"HostInterface_Interfaces_LAN_Autonegotiation_Speed";
	private Set<LanOptions> lanOptions;

	private static final String HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_RETRANSMISSION_COUNT =
			"HostInterface_Interfaces_LAN_Keepalive_RetransmissionCount";
	private int keepCnt;

	private static final String HOST_INTERFACE_INTERFACES_LAN_IPV4_GATEWAY_ADDRESS =
			"HostInterface_Interfaces_LAN_IPv4_GatewayAddress";
	private byte[] gatewayAddress;

	private static final String HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_INTERVAL_TIME =
			"HostInterface_Interfaces_LAN_Keepalive_IntervalTime";
	private int keepInterval;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_IPV4_SUBNET_MASK);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_ENABLE);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DISABLE);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_HOSTNAME_ENABLE);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_IPV4_ENABLE_DHCP);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DUPLEX);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_SPEED);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_RETRANSMISSION_COUNT);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_IPV4_GATEWAY_ADDRESS);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_INTERVAL_TIME);
	}

	public byte[] getSubnetMask() {
		return subnetMask;
	}

	public LanSettings2 withSubnetMask(byte[] subnetMask) {
		this.subnetMask = subnetMask;
		return this;
	}

	public Set<LanOptions> getLanOptions() {
		return lanOptions;
	}

	public LanSettings2 withLanOptions(Set<LanOptions> lanOptions) {
		this.lanOptions = lanOptions;
		return this;
	}

	public int getKeepCnt() {
		return keepCnt;
	}

	public LanSettings2 withKeepCnt(int keepCnt) {
		this.keepCnt = keepCnt;
		return this;
	}

	public byte[] getGatewayAddress() {
		return gatewayAddress;
	}

	public LanSettings2 withGatewayAddress(byte[] gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
		return this;
	}

	public int getKeepInterval() {
		return keepInterval;
	}

	public LanSettings2 withKeepInterval(int keepInterval) {
		this.keepInterval = keepInterval;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(subnetMask);
		buffer.put(LanOptions.getValue(lanOptions));
		buffer.put((byte) keepCnt);
		buffer.put(gatewayAddress);
		buffer.putShort(12, (short) keepInterval);
		return buffer.array();
	}

	@Override
	public LanSettings2 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] subnetMask = new byte[4];
		buffer.get(subnetMask);
		Set<LanOptions> lanOptions = LanOptions.getByValue(buffer.get());
		int keepCnt = Byte.toUnsignedInt(buffer.get());
		byte[] gatewayAddress = new byte[4];
		buffer.get(gatewayAddress);
		int keepInterval = Short.toUnsignedInt(buffer.getShort(12));
		return new LanSettings2().withSubnetMask(subnetMask).withLanOptions(lanOptions).withKeepCnt(keepCnt)
				.withGatewayAddress(gatewayAddress).withKeepInterval(keepInterval);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public boolean updateState(String name, String value) {
		LanSettings2 stored = SerializationUtils.clone(this);
		switch (name) {
			case HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DISABLE: {
				if (Boolean.parseBoolean(value)) {
					lanOptions.add(LanOptions.AUTONEGOTIATION);
				} else {
					lanOptions.remove(LanOptions.AUTONEGOTIATION);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DUPLEX: {
				if (Objects.equals(value.toUpperCase(Locale.getDefault()), "FULL")) {
					lanOptions.add(LanOptions.DUPLEX);
				} else if (Objects.equals(value.toLowerCase(Locale.getDefault()), "HALF")) {
					lanOptions.remove(LanOptions.DUPLEX);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_SPEED: {
				if (Objects.equals(value, "100")) {
					lanOptions.add(LanOptions.SPEED);
				} else if (Objects.equals(value, "10")) {
					lanOptions.remove(LanOptions.SPEED);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_HOSTNAME_ENABLE: {
				if (Boolean.parseBoolean(value)) {
					lanOptions.add(LanOptions.HOSTNAME);
				} else {
					lanOptions.remove(LanOptions.HOSTNAME);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_IPV4_ENABLE_DHCP: {
				if (Boolean.parseBoolean(value)) {
					lanOptions.add(LanOptions.DHCP);
				} else {
					lanOptions.remove(LanOptions.DHCP);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_ENABLE: {
				if (Boolean.parseBoolean(value)) {
					lanOptions.add(LanOptions.KEEP_ALIVE);
				} else {
					lanOptions.remove(LanOptions.KEEP_ALIVE);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_IPV4_SUBNET_MASK: {
				String[] values = DOT.split(value);
				for (int i = 0; i < 4; i++) {
					subnetMask[i] = (byte) Integer.parseInt(values[i]);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_IPV4_GATEWAY_ADDRESS: {
				String[] values = DOT.split(value);
				for (int i = 0; i < 4; i++) {
					gatewayAddress[i] = (byte) Integer.parseInt(values[i]);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_INTERVAL_TIME: {
				keepInterval = Integer.parseInt(value);
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_RETRANSMISSION_COUNT: {
				keepCnt = Integer.parseInt(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(HOST_INTERFACE_INTERFACES_LAN_IPV4_SUBNET_MASK,
				Byte.toUnsignedInt(subnetMask[0]) + "." + Byte.toUnsignedInt(subnetMask[1]) + '.' +
						Byte.toUnsignedInt(subnetMask[2]) + '.' + Byte.toUnsignedInt(subnetMask[3]));
		result.put(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_ENABLE,
				Boolean.toString(lanOptions.contains(LanOptions.KEEP_ALIVE)));
		result.put(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DISABLE,
				Boolean.toString(lanOptions.contains(LanOptions.AUTONEGOTIATION)));
		result.put(HOST_INTERFACE_INTERFACES_LAN_HOSTNAME_ENABLE,
				Boolean.toString(lanOptions.contains(LanOptions.HOSTNAME)));
		result.put(HOST_INTERFACE_INTERFACES_LAN_IPV4_ENABLE_DHCP,
				Boolean.toString(lanOptions.contains(LanOptions.DHCP)));
		result.put(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_DUPLEX,
				lanOptions.contains(LanOptions.DUPLEX) ? "FULL" : "HALF");
		result.put(HOST_INTERFACE_INTERFACES_LAN_AUTONEGOTIATION_SPEED,
				lanOptions.contains(LanOptions.SPEED) ? "100" : "10");
		result.put(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_RETRANSMISSION_COUNT, Integer.toString(keepCnt));
		result.put(HOST_INTERFACE_INTERFACES_LAN_IPV4_GATEWAY_ADDRESS,
				Byte.toUnsignedInt(gatewayAddress[0]) + "." + Byte.toUnsignedInt(gatewayAddress[1]) + '.' +
						Byte.toUnsignedInt(gatewayAddress[2]) + '.' + Byte.toUnsignedInt(gatewayAddress[3]));
		result.put(HOST_INTERFACE_INTERFACES_LAN_KEEPALIVE_INTERVAL_TIME, Integer.toString(keepCnt));
		return result;
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	public enum LanOptions {
		KEEP_ALIVE,
		RESERVED_1,
		RESERVED_2,
		AUTONEGOTIATION,
		HOSTNAME,
		DUPLEX,
		SPEED,
		DHCP;

		public static Set<LanOptions> getByValue(byte... value) {
			Set<LanOptions> result = EnumSet.noneOf(LanOptions.class);
			for (LanOptions item : values()) {
				if ((value[0] >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static byte getValue(Set<LanOptions> value) {
			byte result = 0;
			for (LanOptions item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LanSettings2 that = (LanSettings2) o;
		return keepCnt == that.keepCnt && keepInterval == that.keepInterval && Arrays.equals(subnetMask,
				that.subnetMask) && Objects.equals(lanOptions, that.lanOptions) && Arrays.equals(gatewayAddress,
				that.gatewayAddress);
	}

	@Override
	public int hashCode() {

		int result = Objects.hash(lanOptions, keepCnt, keepInterval);
		result = 31 * result + Arrays.hashCode(subnetMask);
		result = 31 * result + Arrays.hashCode(gatewayAddress);
		return result;
	}
}
