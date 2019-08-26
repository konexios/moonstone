package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;

public class InterfaceAndMode implements ConfigParameter<InterfaceAndMode> {
	private static final long serialVersionUID = 1268129830261873145L;

	private static final int ID = 1;

	private static final String HOST_INTERFACE_INTERFACES_SERIAL_BUS_ADDRESS =
			"HostInterface_Interfaces_Serial_BusAddress";
	private int busAddress;

	private static final String HOST_INTERFACE_INTERFACES_SERIAL_BAUDRATE = "HostInterface_Interfaces_Serial_Baudrate";
	private Baudrate baudrate;

	private static final String HOST_INTERFACE_INTERFACES_SERIAL_PARITY = "HostInterface_Interfaces_Serial_Parity";
	private static final String HOST_INTERFACE_INTERFACES_SERIAL_DATABITS = "HostInterface_Interfaces_Serial_Databits";
	private static final String HOST_INTERFACE_INTERFACES_SERIAL_STOPBITS = "HostInterface_Interfaces_Serial_Stopbits";
	private Parity parity;

	private static final String AIR_INTERFACE_TIME_LIMIT = "AirInterface_TimeLimit";
	private int trResponseTime;

	private static final String OPERATING_MODE_SCAN_MODE_INTERFACE = "OperatingMode_ScanMode_Interface";
	private ScanInterface scanInterface;

	private static final String HOST_INTERFACE_INTERFACES_RS232 = "HostInterface_Interfaces_RS232";
	private static final String HOST_INTERFACE_INTERFACES_LAN = "HostInterface_Interfaces_LAN";
	private static final String HOST_INTERFACE_INTERFACES_USB = "HostInterface_Interfaces_USB";
	private static final String HOST_INTERFACE_INTERFACES_DISCOVERY = "HostInterface_Interfaces_Discovery";
	private Set<Interface> interfaces;

	private static final String OPERATING_MODE_MODE = "OperatingMode_Mode";
	private ReaderMode readerMode;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(HOST_INTERFACE_INTERFACES_SERIAL_BUS_ADDRESS);
		ALL.add(HOST_INTERFACE_INTERFACES_SERIAL_BAUDRATE);
		ALL.add(HOST_INTERFACE_INTERFACES_SERIAL_PARITY);
		ALL.add(HOST_INTERFACE_INTERFACES_SERIAL_DATABITS);
		ALL.add(HOST_INTERFACE_INTERFACES_SERIAL_STOPBITS);
		ALL.add(AIR_INTERFACE_TIME_LIMIT);
		ALL.add(OPERATING_MODE_SCAN_MODE_INTERFACE);
		ALL.add(HOST_INTERFACE_INTERFACES_RS232);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN);
		ALL.add(HOST_INTERFACE_INTERFACES_USB);
		ALL.add(HOST_INTERFACE_INTERFACES_DISCOVERY);
		ALL.add(OPERATING_MODE_MODE);
	}

	public int getBusAddress() {
		return busAddress;
	}

	public InterfaceAndMode withBusAddress(int busAddress) {
		this.busAddress = busAddress;
		return this;
	}

	public Baudrate getBaudrate() {
		return baudrate;
	}

	public InterfaceAndMode withBaudrate(Baudrate baudrate) {
		this.baudrate = baudrate;
		return this;
	}

	public Parity getParity() {
		return parity;
	}

	public InterfaceAndMode withParity(Parity parity) {
		this.parity = parity;
		return this;
	}

	public int getTrResponseTime() {
		return trResponseTime;
	}

	public InterfaceAndMode withTrResponseTime(int trResponseTime) {
		this.trResponseTime = trResponseTime;
		return this;
	}

	public ScanInterface getScanInterface() {
		return scanInterface;
	}

	public InterfaceAndMode withScanInterface(ScanInterface scanInterface) {
		this.scanInterface = scanInterface;
		return this;
	}

	public Set<Interface> getInterfaces() {
		return interfaces;
	}

	public InterfaceAndMode withInterfaces(Set<Interface> interfaces) {
		this.interfaces = interfaces;
		return this;
	}

	public ReaderMode getReaderMode() {
		return readerMode;
	}

	public InterfaceAndMode withReaderMode(ReaderMode readerMode) {
		this.readerMode = readerMode;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put((byte) busAddress);
		buffer.put(2, (byte) baudrate.value);
		buffer.put(3, (byte) parity.ordinal());
		buffer.putShort(6, (short) trResponseTime);
		buffer.put(11, (byte) scanInterface.ordinal());
		buffer.put(12, Interface.getValue(interfaces));
		buffer.put(13, (byte) readerMode.value);
		return buffer.array();
	}

	@Override
	public InterfaceAndMode parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int busAddress = Byte.toUnsignedInt(buffer.get(0));
		Baudrate baudrate = Baudrate.getByValue(Byte.toUnsignedInt(buffer.get(2)));
		Parity parity = Parity.values()[Byte.toUnsignedInt(buffer.get(3))];
		int trResponseTime = Short.toUnsignedInt(buffer.getShort(6));
		ScanInterface scanInterface = ScanInterface.values()[Byte.toUnsignedInt(buffer.get(11))];
		Set<Interface> interfaces = Interface.getByValue(buffer.get(12));
		ReaderMode readerMode = ReaderMode.getByValue(Byte.toUnsignedInt(buffer.get(13)));
		return new InterfaceAndMode().withBusAddress(busAddress).withBaudrate(baudrate).withParity(parity)
				.withTrResponseTime(trResponseTime).withScanInterface(scanInterface).withInterfaces(interfaces)
				.withReaderMode(readerMode);
	}

	@Override
	public int getId() {
		return ID;
	}

	public enum Baudrate {
		BAUD_RATE_4800(0x05),
		BAUD_RATE_9600(0x06),
		BAUD_RATE_19200(0x07),
		BAUD_RATE_38400(0x08),
		BAUD_RATE_57600(0x09),
		BAUD_RATE_115200(0x0a);

		private final int value;

		Baudrate(int value) {
			this.value = value;
		}

		public static Baudrate getByValue(int value) {
			for (Baudrate item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}
	}

	public enum Parity {
		NO_PARITY,
		EVEN_PARITY,
		ODD_PARITY
	}

	public enum ScanInterface {
		RS232,
		RESERVED_1,
		USB,
		DATA_CLOCK,
		RESERVED_4,
		RESERVED_5,
		RESERVED_6,
		RESERVED_7
	}

	public enum Interface {
		RS232(0),
		RS4XX(1),
		LAN(2),
		WLAN(3),
		USB(4),
		DISCOVERY(7);

		private final int index;

		Interface(int index) {
			this.index = index;
		}

		public static Set<Interface> getByValue(byte... value) {
			Set<Interface> result = EnumSet.noneOf(Interface.class);
			for (Interface item : values()) {
				if ((value[0] >> item.index & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static byte getValue(Set<Interface> value) {
			byte result = 0;
			for (Interface item : value) {
				result |= 1 << item.index;
			}
			return result;
		}
	}

	public enum ReaderMode {
		HOST(0b00000000),
		SCAN(0b00000001),
		BUFFERED_READ(0b10000000),
		NOTIFICATION(0b11000000);

		private final int value;

		ReaderMode(int value) {
			this.value = value;
		}

		public static ReaderMode getByValue(int value) {
			for (ReaderMode item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		InterfaceAndMode stored = SerializationUtils.clone(this);
		switch (name) {
			case HOST_INTERFACE_INTERFACES_SERIAL_BUS_ADDRESS: {
				busAddress = Integer.parseInt(value);
				break;
			}
			case HOST_INTERFACE_INTERFACES_SERIAL_BAUDRATE: {
				baudrate = Baudrate.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case HOST_INTERFACE_INTERFACES_SERIAL_PARITY: {
				parity = Parity.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case AIR_INTERFACE_TIME_LIMIT: {
				trResponseTime = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_SCAN_MODE_INTERFACE: {
				scanInterface = ScanInterface.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case HOST_INTERFACE_INTERFACES_RS232: {
				if (Boolean.parseBoolean(value)) {
					interfaces.add(Interface.RS232);
				} else {
					interfaces.remove(Interface.RS232);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN: {
				if (Boolean.parseBoolean(value)) {
					interfaces.add(Interface.LAN);
				} else {
					interfaces.remove(Interface.LAN);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_USB: {
				if (Boolean.parseBoolean(value)) {
					interfaces.add(Interface.USB);
				} else {
					interfaces.remove(Interface.USB);
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_DISCOVERY: {
				if (Boolean.parseBoolean(value)) {
					interfaces.add(Interface.DISCOVERY);
				} else {
					interfaces.remove(Interface.DISCOVERY);
				}
				break;
			}
			case OPERATING_MODE_MODE: {
				readerMode = ReaderMode.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case HOST_INTERFACE_INTERFACES_SERIAL_STOPBITS:
			case HOST_INTERFACE_INTERFACES_SERIAL_DATABITS:
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(HOST_INTERFACE_INTERFACES_SERIAL_BUS_ADDRESS, Integer.toString(busAddress));
		result.put(HOST_INTERFACE_INTERFACES_SERIAL_BAUDRATE, baudrate.name());
		result.put(HOST_INTERFACE_INTERFACES_SERIAL_PARITY, parity.name());
		result.put(AIR_INTERFACE_TIME_LIMIT, Integer.toString(trResponseTime));
		result.put(OPERATING_MODE_SCAN_MODE_INTERFACE, scanInterface.name());
		result.put(HOST_INTERFACE_INTERFACES_RS232, Boolean.toString(interfaces.contains(Interface.RS232)));
		result.put(HOST_INTERFACE_INTERFACES_LAN, Boolean.toString(interfaces.contains(Interface.LAN)));
		result.put(HOST_INTERFACE_INTERFACES_USB, Boolean.toString(interfaces.contains(Interface.USB)));
		result.put(HOST_INTERFACE_INTERFACES_DISCOVERY, Boolean.toString(interfaces.contains(Interface.DISCOVERY)));
		result.put(OPERATING_MODE_MODE, readerMode.name());
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
		InterfaceAndMode that = (InterfaceAndMode) o;
		return busAddress == that.busAddress && trResponseTime == that.trResponseTime && baudrate == that.baudrate &&
				parity == that.parity && scanInterface == that.scanInterface && Objects.equals(interfaces,
				that.interfaces) && readerMode == that.readerMode;
	}

	@Override
	public int hashCode() {

		return Objects.hash(busAddress, baudrate, parity, trResponseTime, scanInterface, interfaces, readerMode);
	}

	@Override
	public String toString() {
		return "InterfaceAndMode{" + "busAddress=" + busAddress + ", baudrate=" + baudrate + ", parity=" + parity +
				", trResponseTime=" + trResponseTime + ", scanInterface=" + scanInterface + ", interfaces=" +
				interfaces + ", readerMode=" + readerMode + '}';
	}
}
