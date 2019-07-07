package com.arrow.selene.device.harting.rfid.command;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class ExtendedHeader implements Serializable {
	private static final long serialVersionUID = -7657485999878804035L;

	public static final int HEADER_SIZE = 5;
	private int length;
	private int comAddress;
	private int control;

	public int getLength() {
		return length;
	}

	public ExtendedHeader withLength(int length) {
		this.length = length;
		return this;
	}

	public int getComAddress() {
		return comAddress;
	}

	public ExtendedHeader withComAddress(int comAddress) {
		this.comAddress = comAddress;
		return this;
	}

	public int getControl() {
		return control;
	}

	public ExtendedHeader withControl(int control) {
		this.control = control;
		return this;
	}

	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
		buffer.put(0, (byte) 2);
		buffer.putShort(1, (short) (HEADER_SIZE + length + 2));
		buffer.put(3, (byte) comAddress);
		buffer.put(4, (byte) control);
		return buffer.array();
	}

	@SuppressWarnings("unused")
	public void extractHeader(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
	}
}
