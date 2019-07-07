package com.arrow.selene.device.harting.rfid.command;

public class BaudRateDetection extends AbstractExtendedCommand {
	private static final long serialVersionUID = 2309441716324342954L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x52;
	private static final byte[] PAYLOAD = {0};

	@Override
	protected byte[] buildPayload() {
		return PAYLOAD;
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}
}
