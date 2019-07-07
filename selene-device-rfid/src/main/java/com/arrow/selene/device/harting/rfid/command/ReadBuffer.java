package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class ReadBuffer extends AbstractExtendedCommand {
	private static final long serialVersionUID = -7929733089024619115L;
	private static final int LENGTH = 2;
	private static final int CONTROL = 0x22;
	private int dataSets;

	public int getDataSets() {
		return dataSets;
	}

	public void setDataSets(int dataSets) {
		this.dataSets = dataSets;
	}

	public ReadBuffer withDataSets(int dataSets) {
		this.dataSets = dataSets;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(LENGTH);
		buffer.putShort((short) dataSets);
		return buffer.array();
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
