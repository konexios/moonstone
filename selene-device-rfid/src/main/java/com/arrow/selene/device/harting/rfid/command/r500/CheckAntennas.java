package com.arrow.selene.device.harting.rfid.command.r500;

import com.arrow.selene.device.harting.rfid.command.AbstractExtendedCommand;

public class CheckAntennas extends AbstractExtendedCommand {
	private static final long serialVersionUID = -3539624413565392290L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x76;
	private byte[] payload = new byte[1];
	private int mode;

	@Override
	protected byte[] buildPayload() {
		payload[0] = (byte) mode;
		return payload;
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
