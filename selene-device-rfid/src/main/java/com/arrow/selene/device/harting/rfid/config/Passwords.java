package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Passwords implements ConfigParameter<Passwords> {
	private static final long serialVersionUID = -539702393027278609L;

	private static final int ID = 0;

	private byte[] readerId;
	private int cfgAccess;

	private static final List<String> ALL = new ArrayList<>();

	public byte[] getReaderId() {
		return readerId;
	}

	public Passwords withReaderId(byte[] readerId) {
		this.readerId = readerId;
		return this;
	}

	public int getCfgAccess() {
		return cfgAccess;
	}

	public Passwords withCfgAccess(int cfgAccess) {
		this.cfgAccess = cfgAccess;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(readerId);
		buffer.putInt(8, cfgAccess);
		return buffer.array();
	}

	@Override
	public Passwords parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] readerId = new byte[4];
		buffer.get(readerId);
		int cfgAccess = buffer.getInt(8);
		return new Passwords().withReaderId(readerId).withCfgAccess(cfgAccess);
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
		Passwords passwords = (Passwords) o;
		return cfgAccess == passwords.cfgAccess && Arrays.equals(readerId, passwords.readerId);
	}

	@Override
	public int hashCode() {

		int result = Objects.hash(cfgAccess);
		result = 31 * result + Arrays.hashCode(readerId);
		return result;
	}
}
