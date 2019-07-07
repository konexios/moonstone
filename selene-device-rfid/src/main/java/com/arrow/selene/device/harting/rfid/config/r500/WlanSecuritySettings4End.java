package com.arrow.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings4End implements ConfigParameter<WlanSecuritySettings4End> {
	private static final long serialVersionUID = 4517394617708101017L;

	private byte[] wpa2Key;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings4End(byte[] wpa2Key) {
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
	public WlanSecuritySettings4End parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] wepKey = new byte[3];
		buffer.get(wepKey);
		return new WlanSecuritySettings4End(wepKey);
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
		WlanSecuritySettings4End that = (WlanSecuritySettings4End) o;
		return Arrays.equals(wpa2Key, that.wpa2Key);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wpa2Key);
	}
}
