package com.arrow.selene.device.harting.rfid.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelectionMask1Ext implements ConfigParameter<SelectionMask1Ext> {
	private static final long serialVersionUID = -6492720097693178133L;

	private static final int ID = 23;

	private byte[] mask;

	private static final List<String> ALL = new ArrayList<>();

	public SelectionMask1Ext(byte[] mask) {
		this.mask = mask;
	}

	@Override
	public byte[] build() {
		return mask;
	}

	@Override
	public SelectionMask1Ext parse(byte... payload) {
		return new SelectionMask1Ext(payload);
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
		SelectionMask1Ext that = (SelectionMask1Ext) o;
		return Arrays.equals(mask, that.mask);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mask);
	}
}
