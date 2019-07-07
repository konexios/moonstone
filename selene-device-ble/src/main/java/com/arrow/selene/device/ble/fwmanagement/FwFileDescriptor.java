package com.arrow.selene.device.ble.fwmanagement;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FwFileDescriptor {

	private long fileLength;
	private byte[] fileBytes;

	public FwFileDescriptor(byte[] bytes) {
		fileBytes = bytes;
		setFileLengthBytes();
	}

	private void setFileLengthBytes() {
		if (fileBytes != null) {
			fileLength = fileBytes.length;
		} else {
			fileLength = 0;
		}
	}

	public long getLength() {
		return fileLength;
	}

	public InputStream openFile() {
		return new ByteArrayInputStream(fileBytes);
	}

}
