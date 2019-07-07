package com.arrow.selene.device.harting.rfid.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserParameter implements ConfigParameter<UserParameter> {
	private static final long serialVersionUID = 565069648079855237L;

	private static final int ID = 63;

	private byte[] bytes = new byte[14];

	private static final List<String> ALL = new ArrayList<>();

	public static int getID() {
		return ID;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public UserParameter withBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}

	@Override
	public byte[] build() {
		return bytes;
	}

	@Override
	public UserParameter parse(byte... payload) {
		return new UserParameter().withBytes(payload);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public boolean updateState(String name, String value) {
		return false;
	}

	@Override
	public Map<String, String> getStates() {
		return Collections.emptyMap();
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserParameter that = (UserParameter) o;
		return Arrays.equals(bytes, that.bytes);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes);
	}
}
