package moonstone.selene.device.harting.rfid.command;

public class ReadConfiguration extends AbstractExtendedCommand {
	private static final long serialVersionUID = 6017435387428143405L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x80;
	private CfgAddress cfgAddress;

	public CfgAddress getCfgAddress() {
		return cfgAddress;
	}

	public void setCfgAddress(CfgAddress cfgAddress) {
		this.cfgAddress = cfgAddress;
	}

	public ReadConfiguration withCfgAddress(CfgAddress cfgAddress) {
		this.cfgAddress = cfgAddress;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		return new byte[]{(byte) CfgAddress.build(cfgAddress)};
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}

	public static class CfgAddress {
		private Location location;
		private int address;

		public Location getLocation() {
			return location;
		}

		public int getAddress() {
			return address;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public void setAddress(int address) {
			this.address = address;
		}

		public CfgAddress withLocation(Location location) {
			this.location = location;
			return this;
		}

		public CfgAddress withAddress(int address) {
			this.address = address;
			return this;
		}

		public static int build(CfgAddress value) {
			int result = 0;
			result |= Location.build(value.location) << 7;
			result |= value.address;
			return result;
		}
	}

	public enum Location {
		RAM,
		EEPROM;

		public static int build(Location value) {
			return value.ordinal();
		}
	}
}
