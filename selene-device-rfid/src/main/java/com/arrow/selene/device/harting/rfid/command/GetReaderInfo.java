package com.arrow.selene.device.harting.rfid.command;

public class GetReaderInfo extends AbstractExtendedCommand {
	private static final long serialVersionUID = 8885170219272317136L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x66;
	private Mode mode;

	public Mode getMode() {
		return mode;
	}

	public GetReaderInfo withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
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
		return mode.value;
	}

	public enum Mode {
		RF_CONTROLLER_FIRMWARE(0x00),
		RF_DECODER_FIRMWARE(0x03),
		ADDITIONAL_FIRMWARE_FUNCTIONS(0x04),
		RFC_BOOTLOADER_FIRMWARE(0x05),
		HARDWARE_INFORMATION(0x10),
		SAM_INFORMATION(0x11),
		CPU_INFORMATION(0x12),
		RF_STACK_INFORMATION(0x15),
		IDT_STACK_INFORMATION(0x16),
		CFG_INFORMATION_FOR_READ(0x40),
		CFG_INFORMATION_FOR_WRITE(0x41),
		LAN_INFORMATION_MAC(0x50),
		LAN_INFORMATION_IP_ADDRESS(0x51),
		LAN_INFORMATION_NETMASK(0x52),
		LAN_INFORMATION_GATEWAY_ADDRESS(0x53),
		IO_CAPABILITIES(0x60),
		DEVICE_ID(0x80),
		ALL(0xff);

		private final int value;

		Mode(int value) {
			this.value = value;
		}

		public static int build(Mode mode) {
			return mode.value;
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
	}
}
