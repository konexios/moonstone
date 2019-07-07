package com.arrow.selene.device.harting.rfid.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelectionMask3Ext implements ConfigParameter<SelectionMask3Ext> {
	private static final long serialVersionUID = 4530028453327098066L;

	private static final int ID = 27;

	private byte[] mask;

	private static final List<String> ALL = new ArrayList<>();

	public SelectionMask3Ext(byte[] mask) {
		this.mask = mask;
	}

	@Override
	public byte[] build() {
		return mask;
	}

	@Override
	public SelectionMask3Ext parse(byte... payload) {
		return new SelectionMask3Ext(payload);
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
		SelectionMask3Ext that = (SelectionMask3Ext) o;
		return Arrays.equals(mask, that.mask);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mask);
	}
}
