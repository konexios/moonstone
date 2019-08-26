package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import moonstone.selene.device.harting.rfid.Utils;
import moonstone.selene.device.harting.rfid.command.GetReaderInfo.Mode;

public class GetReaderInfoResponse implements Response<GetReaderInfoResponse> {
	private static final long serialVersionUID = -1783349791214733760L;
	private int status;
	private Mode0 mode0;
	private byte[] rfDecoderInformation;
	private byte[] additionalFirmwareFunctions;
	private String rfcBootloaderFirmware;
	private Mode10 mode10;
	private int numberOfSamSlots;
	private Mode12 mode12;
	private String rfStackRevision;
	private String idtStackRevision;
	private Set<Integer> readPermissions;
	private Set<Integer> writePermissions;
	private Mode50 mode50;
	private Mode51 mode51;
	private Mode52 mode52;
	private Mode53 mode53;
	private Mode60 mode60;
	private Mode80 mode80;

	public int getStatus() {
		return status;
	}

	public Mode0 getMode0() {
		return mode0;
	}

	public byte[] getRfDecoderInformation() {
		return rfDecoderInformation;
	}

	public byte[] getAdditionalFirmwareFunctions() {
		return additionalFirmwareFunctions;
	}

	public String getRfcBootloaderFirmware() {
		return rfcBootloaderFirmware;
	}

	public Mode10 getMode10() {
		return mode10;
	}

	public int getNumberOfSamSlots() {
		return numberOfSamSlots;
	}

	public Mode12 getMode12() {
		return mode12;
	}

	public String getRfStackRevision() {
		return rfStackRevision;
	}

	public String getIdtStackRevision() {
		return idtStackRevision;
	}

	public Set<Integer> getReadPermissions() {
		return readPermissions;
	}

	public Set<Integer> getWritePermissions() {
		return writePermissions;
	}

	public Mode50 getMode50() {
		return mode50;
	}

	public Mode51 getMode51() {
		return mode51;
	}

	public Mode52 getMode52() {
		return mode52;
	}

	public Mode53 getMode53() {
		return mode53;
	}

	public Mode60 getMode60() {
		return mode60;
	}

	public Mode80 getMode80() {
		return mode80;
	}

	public GetReaderInfoResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public GetReaderInfoResponse withMode0(Mode0 mode0) {
		this.mode0 = mode0;
		return this;
	}

	public GetReaderInfoResponse withRfDecoderInformation(byte[] rfDecoderInformation) {
		this.rfDecoderInformation = rfDecoderInformation;
		return this;
	}

	public GetReaderInfoResponse withAdditionalFirmwareFunctions(byte[] additionalFirmwareFunctions) {
		this.additionalFirmwareFunctions = additionalFirmwareFunctions;
		return this;
	}

	public GetReaderInfoResponse withRfcBootloaderFirmware(String rfcBootloaderFirmware) {
		this.rfcBootloaderFirmware = rfcBootloaderFirmware;
		return this;
	}

	public GetReaderInfoResponse withMode10(Mode10 mode10) {
		this.mode10 = mode10;
		return this;
	}

	public GetReaderInfoResponse withNumberOfSamSlots(int numberOfSamSlots) {
		this.numberOfSamSlots = numberOfSamSlots;
		return this;
	}

	public GetReaderInfoResponse withMode12(Mode12 mode12) {
		this.mode12 = mode12;
		return this;
	}

	public GetReaderInfoResponse withRfStackRevision(String rfStackRevision) {
		this.rfStackRevision = rfStackRevision;
		return this;
	}

	public GetReaderInfoResponse withIdtStackRevision(String idtStackRevision) {
		this.idtStackRevision = idtStackRevision;
		return this;
	}

	public GetReaderInfoResponse withReadPermissions(Set<Integer> readPermissions) {
		this.readPermissions = readPermissions;
		return this;
	}

	public GetReaderInfoResponse withWritePermissions(Set<Integer> writePermissions) {
		this.writePermissions = writePermissions;
		return this;
	}

	public GetReaderInfoResponse withMode50(Mode50 mode50) {
		this.mode50 = mode50;
		return this;
	}

	public GetReaderInfoResponse withMode51(Mode51 mode51) {
		this.mode51 = mode51;
		return this;
	}

	public GetReaderInfoResponse withMode52(Mode52 mode52) {
		this.mode52 = mode52;
		return this;
	}

	public GetReaderInfoResponse withMode53(Mode53 mode53) {
		this.mode53 = mode53;
		return this;
	}

	public GetReaderInfoResponse withMode60(Mode60 mode60) {
		this.mode60 = mode60;
		return this;
	}

	public GetReaderInfoResponse withMode80(Mode80 mode80) {
		this.mode80 = mode80;
		return this;
	}

	public static class Mode0 {
		private String softwareRevision;
		private int developmentRevision;
		private int hardwareType;
		private int softwareType;
		private int transpondersType;
		private int rxBuffer;
		private int txBuffer;

		public String getSoftwareRevision() {
			return softwareRevision;
		}

		public Mode0 withSoftwareRevision(String softwareRevision) {
			this.softwareRevision = softwareRevision;
			return this;
		}

		public int getDevelopmentRevision() {
			return developmentRevision;
		}

		public Mode0 withDevelopmentRevision(int developmentRevision) {
			this.developmentRevision = developmentRevision;
			return this;
		}

		public int getHardwareType() {
			return hardwareType;
		}

		public Mode0 withHardwareType(int hardwareType) {
			this.hardwareType = hardwareType;
			return this;
		}

		public int getSoftwareType() {
			return softwareType;
		}

		public Mode0 withSoftwareType(int softwareType) {
			this.softwareType = softwareType;
			return this;
		}

		public int getTranspondersType() {
			return transpondersType;
		}

		public Mode0 withTranspondersType(int transpondersType) {
			this.transpondersType = transpondersType;
			return this;
		}

		public int getRxBuffer() {
			return rxBuffer;
		}

		public Mode0 withRxBuffer(int rxBuffer) {
			this.rxBuffer = rxBuffer;
			return this;
		}

		public int getTxBuffer() {
			return txBuffer;
		}

		public Mode0 withTxBuffer(int txBuffer) {
			this.txBuffer = txBuffer;
			return this;
		}

		public static Mode0 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			byte[] bytes = new byte[2];
			buffer.get(bytes);
			String softwareRevision = Utils.joinBytes(".", bytes);
			int developmentRevision = Byte.toUnsignedInt(buffer.get());
			int hardwareType = Byte.toUnsignedInt(buffer.get());
			int softwareType = Byte.toUnsignedInt(buffer.get());
			int transpondersType = Short.toUnsignedInt(buffer.getShort());
			int rxBuffer = Short.toUnsignedInt(buffer.getShort());
			int txBuffer = Short.toUnsignedInt(buffer.getShort());
			return new Mode0().withSoftwareRevision(softwareRevision).withDevelopmentRevision(developmentRevision)
					.withHardwareType(hardwareType).withSoftwareType(softwareType).withTranspondersType(
							transpondersType).withRxBuffer(rxBuffer).withTxBuffer(txBuffer);
		}
	}

	public static class Mode10 {
		private int hardwareInfo;
		private int digitalHardware;
		private int analogHardware;
		private Set<Frequency> frequencies;
		private Set<PortType> portTypes;

		public int getHardwareInfo() {
			return hardwareInfo;
		}

		public Mode10 withHardwareInfo(int hardwareInfo) {
			this.hardwareInfo = hardwareInfo;
			return this;
		}

		public int getDigitalHardware() {
			return digitalHardware;
		}

		public Mode10 withDigitalHardware(int digitalHardware) {
			this.digitalHardware = digitalHardware;
			return this;
		}

		public int getAnalogHardware() {
			return analogHardware;
		}

		public Mode10 withAnalogHardware(int analogHardware) {
			this.analogHardware = analogHardware;
			return this;
		}

		public Set<Frequency> getFrequencies() {
			return frequencies;
		}

		public Mode10 withFrequencies(Set<Frequency> frequencies) {
			this.frequencies = frequencies;
			return this;
		}

		public Set<PortType> getPortTypes() {
			return portTypes;
		}

		public Mode10 withPortTypes(Set<PortType> portTypes) {
			this.portTypes = portTypes;
			return this;
		}

		public static Mode10 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			int hardwareInfo = Short.toUnsignedInt(buffer.getShort());
			int digitalHardware = Short.toUnsignedInt(buffer.getShort());
			int analogHardware = Short.toUnsignedInt(buffer.getShort());
			Set<Frequency> frequencies = Frequency.parse(Byte.toUnsignedInt(buffer.get()));
			Set<PortType> portTypes = PortType.parse(Byte.toUnsignedInt(buffer.get()));
			return new Mode10().withHardwareInfo(hardwareInfo).withDigitalHardware(digitalHardware).withAnalogHardware(
					analogHardware).withFrequencies(frequencies).withPortTypes(portTypes);
		}
	}

	public static class Mode12 {
		private int manufacturer;
		private int type;
		private int revision;
		private long flashSize;

		public int getManufacturer() {
			return manufacturer;
		}

		public int getType() {
			return type;
		}

		public int getRevision() {
			return revision;
		}

		public long getFlashSize() {
			return flashSize;
		}

		public Mode12 withManufacturer(int manufacturer) {
			this.manufacturer = manufacturer;
			return this;
		}

		public Mode12 withType(int type) {
			this.type = type;
			return this;
		}

		public Mode12 withRevision(int revision) {
			this.revision = revision;
			return this;
		}

		public Mode12 withFlashSize(long flashSize) {
			this.flashSize = flashSize;
			return this;
		}

		public static Mode12 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			int manufacturer = Byte.toUnsignedInt(buffer.get());
			int type = Short.toUnsignedInt(buffer.getShort());
			int revision = Byte.toUnsignedInt(buffer.get());
			long flashSize = Integer.toUnsignedLong(buffer.getInt());
			return new Mode12().withManufacturer(manufacturer).withType(type).withRevision(revision).withFlashSize(
					flashSize);
		}
	}

	public static class Mode50 {
		private Set<Flag> flags;
		private String mac;

		public Set<Flag> getFlags() {
			return flags;
		}

		public Mode50 withFlags(Set<Flag> flags) {
			this.flags = flags;
			return this;
		}

		public String getMac() {
			return mac;
		}

		public Mode50 withMac(String mac) {
			this.mac = mac;
			return this;
		}

		public static Mode50 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Set<Flag> flags = Flag.parse(Byte.toUnsignedInt(buffer.get()));
			byte[] bytes = new byte[6];
			buffer.get(bytes);
			return new Mode50().withFlags(flags).withMac(Utils.joinBytesHex(":", bytes));
		}
	}

	public static class Mode51 {
		private Set<Flag> flags;
		private String ipAddress;

		public Set<Flag> getFlags() {
			return flags;
		}

		public Mode51 withFlags(Set<Flag> flags) {
			this.flags = flags;
			return this;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public Mode51 withIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
			return this;
		}

		public static Mode51 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Set<Flag> flags = Flag.parse(Byte.toUnsignedInt(buffer.get()));
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			return new Mode51().withFlags(flags).withIpAddress(Utils.joinBytes(".", bytes));
		}
	}

	public static class Mode52 {
		private Set<Flag> flags;
		private String netmask;

		public Set<Flag> getFlags() {
			return flags;
		}

		public Mode52 withFlags(Set<Flag> flags) {
			this.flags = flags;
			return this;
		}

		public String getNetmask() {
			return netmask;
		}

		public Mode52 withNetmask(String netmask) {
			this.netmask = netmask;
			return this;
		}

		public static Mode52 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Set<Flag> flags = Flag.parse(Byte.toUnsignedInt(buffer.get()));
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			return new Mode52().withFlags(flags).withNetmask(Utils.joinBytes(".", bytes));
		}
	}

	public static class Mode53 {
		private Set<Flag> flags;
		private String gateway;

		public Set<Flag> getFlags() {
			return flags;
		}

		public Mode53 withFlags(Set<Flag> flags) {
			this.flags = flags;
			return this;
		}

		public String getGateway() {
			return gateway;
		}

		public Mode53 withGateway(String gateway) {
			this.gateway = gateway;
			return this;
		}

		public static Mode53 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Set<Flag> flags = Flag.parse(Byte.toUnsignedInt(buffer.get()));
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			return new Mode53().withFlags(flags).withGateway(Utils.joinBytes(".", bytes));
		}
	}

	public static class Mode60 {
		private int numberOfInputs;
		private int numberOfOutputs;
		private int numberOfRelays;

		public int getNumberOfInputs() {
			return numberOfInputs;
		}

		public Mode60 withNumberOfInputs(int numberOfInputs) {
			this.numberOfInputs = numberOfInputs;
			return this;
		}

		public int getNumberOfOutputs() {
			return numberOfOutputs;
		}

		public Mode60 withNumberOfOutputs(int numberOfOutputs) {
			this.numberOfOutputs = numberOfOutputs;
			return this;
		}

		public int getNumberOfRelays() {
			return numberOfRelays;
		}

		public Mode60 withNumberOfRelays(int numberOfRelays) {
			this.numberOfRelays = numberOfRelays;
			return this;
		}

		public static Mode60 parse(byte... data) {
			int numberOfInputs = Byte.toUnsignedInt(data[0]);
			int numberOfOutputs = Byte.toUnsignedInt(data[1]);
			int numberOfRelays = Byte.toUnsignedInt(data[2]);
			return new Mode60().withNumberOfInputs(numberOfInputs).withNumberOfOutputs(numberOfOutputs)
					.withNumberOfRelays(numberOfRelays);
		}
	}

	public static class Mode80 {
		private long deviceId;
		private long customerFirmware;
		private int firmwareVersion;
		private int transponderDrivers;
		private int functions;

		public long getDeviceId() {
			return deviceId;
		}

		public Mode80 withDeviceId(long deviceId) {
			this.deviceId = deviceId;
			return this;
		}

		public long getCustomerFirmware() {
			return customerFirmware;
		}

		public Mode80 withCustomerFirmware(long customerFirmware) {
			this.customerFirmware = customerFirmware;
			return this;
		}

		public long getFirmwareVersion() {
			return firmwareVersion;
		}

		public Mode80 withFirmwareVersion(int firmwareVersion) {
			this.firmwareVersion = firmwareVersion;
			return this;
		}

		public long getTransponderDrivers() {
			return transponderDrivers;
		}

		public Mode80 withTransponderDrivers(int transponderDrivers) {
			this.transponderDrivers = transponderDrivers;
			return this;
		}

		public long getFunctions() {
			return functions;
		}

		public Mode80 withFunctions(int functions) {
			this.functions = functions;
			return this;
		}

		public static Mode80 parse(byte... data) {
			ByteBuffer buffer = ByteBuffer.wrap(data);
			long deviceId = Integer.toUnsignedLong(buffer.getInt());
			long customerFirmware = Integer.toUnsignedLong(buffer.getInt());
			int firmwareVersion = Short.toUnsignedInt(buffer.getShort());
			int transponderDrivers = Short.toUnsignedInt(buffer.getShort());
			int functions = Short.toUnsignedInt(buffer.getShort());
			return new Mode80().withDeviceId(deviceId).withCustomerFirmware(customerFirmware).withFirmwareVersion(
					firmwareVersion).withTransponderDrivers(transponderDrivers).withFunctions(functions);
		}
	}

	public enum Frequency {
		EU(0x01),
		FCC(0x02),
		UHF(0x40),
		HF(0x80);

		private final int value;

		Frequency(int value) {
			this.value = value;
		}

		public static Set<Frequency> parse(int value) {
			Set<Frequency> result = EnumSet.noneOf(Frequency.class);
			for (Frequency item : values()) {
				if ((value & item.value) == item.value) {
					result.add(item);
				}
			}
			return result;
		}
	}

	public enum PortType {
		RS232(0x01),
		RS4XX(0x02),
		LAN(0x04),
		WLAN(0x08),
		USB(0x10),
		BT(0x20),
		DISC(0x80);

		private final int value;

		PortType(int value) {
			this.value = value;
		}

		public static Set<PortType> parse(int value) {
			Set<PortType> result = EnumSet.noneOf(PortType.class);
			for (PortType item : values()) {
				if ((value & item.value) == item.value) {
					result.add(item);
				}
			}
			return result;
		}
	}

	public enum Flag {
		SUPPORTED_V4(0x02),
		DISABLED_V4(0x08),
		DHCP_V4(0x20);

		private final int value;

		Flag(int value) {
			this.value = value;
		}

		public static Set<Flag> parse(int value) {
			Set<Flag> result = EnumSet.noneOf(Flag.class);
			for (Flag item : values()) {
				if ((value & item.value) == item.value) {
					result.add(item);
				}
			}
			return result;
		}
	}

	@Override
	public GetReaderInfoResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		GetReaderInfoResponse result = new GetReaderInfoResponse().withStatus(status);
		boolean all = mode == Mode.ALL.getValue();
		int shift = all ? 4 : 3;
		byte[] bytes;
		do {
			buffer.position(shift);
			Mode byValue = all ? Mode.getByValue(Byte.toUnsignedInt(buffer.get())) : Mode.getByValue(mode);
			switch (byValue) {
				case RF_CONTROLLER_FIRMWARE: {
					bytes = new byte[11];
					buffer.get(bytes);
					result.withMode0(Mode0.parse(bytes));
					break;
				}
				case RF_DECODER_FIRMWARE: {
					bytes = new byte[11];
					buffer.get(bytes);
					result.withRfDecoderInformation(bytes);
					break;
				}
				case ADDITIONAL_FIRMWARE_FUNCTIONS: {
					bytes = new byte[11];
					buffer.get(bytes);
					result.withAdditionalFirmwareFunctions(bytes);
					break;
				}
				case RFC_BOOTLOADER_FIRMWARE: {
					bytes = new byte[2];
					buffer.get(bytes);
					result.withRfcBootloaderFirmware(Utils.joinBytes(".", bytes));
					break;
				}
				case HARDWARE_INFORMATION: {
					bytes = new byte[8];
					buffer.get(bytes);
					result.withMode10(Mode10.parse(bytes));
					break;
				}
				case SAM_INFORMATION: {
					result.withNumberOfSamSlots(Byte.toUnsignedInt(buffer.get()));
					break;
				}
				case CPU_INFORMATION: {
					bytes = new byte[8];
					buffer.get(bytes);
					result.withMode12(Mode12.parse(bytes));
					break;
				}
				case RF_STACK_INFORMATION: {
					bytes = new byte[2];
					buffer.get(bytes);
					result.withRfStackRevision(Utils.joinBytes(".", bytes));
					break;
				}
				case IDT_STACK_INFORMATION: {
					bytes = new byte[2];
					buffer.get(bytes);
					result.withIdtStackRevision(Utils.joinBytes(".", bytes));
					break;
				}
				case CFG_INFORMATION_FOR_READ: {
					bytes = new byte[(Short.toUnsignedInt(buffer.getShort()) + 7) / 8];
					buffer.get(bytes);
					result.withReadPermissions(parsePermissions(bytes));
					break;
				}
				case CFG_INFORMATION_FOR_WRITE: {
					bytes = new byte[(Short.toUnsignedInt(buffer.getShort()) + 7) / 8];
					buffer.get(bytes);
					result.withWritePermissions(parsePermissions(bytes));
					break;
				}
				case LAN_INFORMATION_MAC: {
					bytes = new byte[7];
					buffer.get(bytes);
					result.withMode50(Mode50.parse(bytes));
					break;
				}
				case LAN_INFORMATION_IP_ADDRESS: {
					bytes = new byte[5];
					buffer.get(bytes);
					result.withMode51(Mode51.parse(bytes));
					break;
				}
				case LAN_INFORMATION_NETMASK: {
					bytes = new byte[5];
					buffer.get(bytes);
					result.withMode52(Mode52.parse(bytes));
					break;
				}
				case LAN_INFORMATION_GATEWAY_ADDRESS: {
					bytes = new byte[5];
					buffer.get(bytes);
					result.withMode53(Mode53.parse(bytes));
					break;
				}
				case IO_CAPABILITIES: {
					bytes = new byte[3];
					buffer.get(bytes);
					result.withMode60(Mode60.parse(bytes));
					break;
				}
				case DEVICE_ID: {
					bytes = new byte[14];
					buffer.get(bytes);
					result.withMode80(Mode80.parse(bytes));
					break;
				}
				case ALL: {
					// ignore
					break;
				}
			}
			shift += 31;
		} while (shift < buffer.limit() - 2);
		return result;
	}

	private static Set<Integer> parsePermissions(byte... bytes) {
		Set<Integer> result = new HashSet<>();
		int cfg = 0;
		for (byte b : bytes) {
			for (int i = 0; i < 8; i++) {
				byte value = b;
				if ((value << i & 0x80) == 0x80) {
					result.add(cfg);
				}
				cfg++;
			}
		}
		return result;
	}
}
