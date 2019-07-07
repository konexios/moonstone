package com.arrow.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings3End implements ConfigParameter<WlanSecuritySettings3End> {
	private static final long serialVersionUID = -6441401770309284173L;

	private byte[] wpaKey;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings3End(byte[] wpaKey) {
		this.wpaKey = wpaKey;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) wpaKey.length);
		buffer.put(wpaKey);
		return buffer.array();
	}

	@Override
	public WlanSecuritySettings3End parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] wepKey = new byte[4];
		buffer.get(wepKey);
		return new WlanSecuritySettings3End(wepKey);
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
		WlanSecuritySettings3End that = (WlanSecuritySettings3End) o;
		return Arrays.equals(wpaKey, that.wpaKey);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wpaKey);
	}
}
