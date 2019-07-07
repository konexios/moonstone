package com.arrow.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings4 implements ConfigParameter<WlanSecuritySettings4> {
	private static final long serialVersionUID = 5966714480191553946L;

	private byte[] wpa2Key;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings4(byte[] wpa2Key) {
		this.wpa2Key = wpa2Key;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) wpa2Key.length);
		buffer.put(wpa2Key);
		return buffer.array();
	}

	@Override
	public WlanSecuritySettings4 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] wepKey = new byte[buffer.get(0)];
		buffer.get(wepKey);
		return new WlanSecuritySettings4(wepKey);
	}

	@Override
	public int getId() {
		return 0;
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
		WlanSecuritySettings4 that = (WlanSecuritySettings4) o;
		return Arrays.equals(wpa2Key, that.wpa2Key);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wpa2Key);
	}
}
