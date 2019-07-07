package com.arrow.selene.device.harting.rfid.config.r500;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings4Cont implements ConfigParameter<WlanSecuritySettings4Cont> {
	private static final long serialVersionUID = -3475577027355449028L;

	private byte[] wpa2Key;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings4Cont(byte[] wpa2Key) {
		this.wpa2Key = wpa2Key;
	}

	@Override
	public byte[] build() {
		return wpa2Key;
	}

	@Override
	public WlanSecuritySettings4Cont parse(byte... payload) {
		return new WlanSecuritySettings4Cont(payload);
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
		WlanSecuritySettings4Cont that = (WlanSecuritySettings4Cont) o;
		return Arrays.equals(wpa2Key, that.wpa2Key);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wpa2Key);
	}
}
