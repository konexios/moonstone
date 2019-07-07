package com.arrow.selene.device.harting.rfid.command;

public class ReaderDiagnostic extends AbstractExtendedCommand {
	private static final long serialVersionUID = -6685609504946067821L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x6e;
	private Mode mode;

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public ReaderDiagnostic withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		return new byte[]{(byte) Mode.build(mode)};
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}

	@Override
	public int getCommandMode() {
		return mode.getValue();
	}

	public enum Mode {
		LISTING_FOR_STATUS_0X84_RF_COMMUNICATION_ERROR(0x01),
		LISTING_FOR_STATUS_0X10_EEPROM_FAILURE(0x04),
		LISTING_FOR_FLAG_A(0x05),
		LISTING_FOR_STATUS_0X18_WRONG_FIRMWARE(0x20),
		ALL(0xff);

		private final int value;

		Mode(int value) {
			this.value = value;
		}

		public static Mode getByValue(int value) {
			for (Mode item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}

		public int getValue() {
			return value;
		}

		public static int build(Mode mode) {
			return mode.value;
		}
	}
}
