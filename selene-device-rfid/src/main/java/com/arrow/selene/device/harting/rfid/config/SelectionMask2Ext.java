package com.arrow.selene.device.harting.rfid.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelectionMask2Ext implements ConfigParameter<SelectionMask2Ext> {
	private static final long serialVersionUID = 2809944233956177527L;

	private static final int ID = 25;

	private byte[] mask;

	private static final List<String> ALL = new ArrayList<>();

	public SelectionMask2Ext(byte[] mask) {
		this.mask = mask;
	}

	@Override
	public byte[] build() {
		return mask;
	}

	@Override
	public SelectionMask2Ext parse(byte... payload) {
		return new SelectionMask2Ext(payload);
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
		SelectionMask2Ext that = (SelectionMask2Ext) o;
		return Arrays.equals(mask, that.mask);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(mask);
	}
}
