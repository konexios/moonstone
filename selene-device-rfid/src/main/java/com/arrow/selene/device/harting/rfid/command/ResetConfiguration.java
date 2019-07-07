package com.arrow.selene.device.harting.rfid.command;

public class ResetConfiguration extends AbstractExtendedCommand {
	private static final long serialVersionUID = -1623194730476695789L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x8c;
	private CfgAddress cfgAddress;

	public void setCfgAddress(CfgAddress cfgAddress) {
		this.cfgAddress = cfgAddress;
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

		public void setLocation(Location location) {
			this.location = location;
		}

		public void setAddress(int address) {
			this.address = address;
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
