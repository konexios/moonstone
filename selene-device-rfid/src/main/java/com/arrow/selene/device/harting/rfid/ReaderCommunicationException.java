package com.arrow.selene.device.harting.rfid;

import java.io.IOException;

public class ReaderCommunicationException extends IOException {
	private static final long serialVersionUID = 3779391558717508333L;

	public ReaderCommunicationException(String message) {
		super(message);
	}

	public ReaderCommunicationException(Throwable cause) {
		super(cause);
	}
}
