package com.arrow.selene.device.harting.rfid.command;

import com.arrow.selene.engine.Utils;

public class ReadDataBufferInfo extends AbstractExtendedCommand {
	private static final long serialVersionUID = -3819458207712184108L;
	private static final int LENGTH = 0;
	private static final int CONTROL = 0x31;

	@Override
	protected byte[] buildPayload() {
		return Utils.EMPTY_BYTE_ARRAY;
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
