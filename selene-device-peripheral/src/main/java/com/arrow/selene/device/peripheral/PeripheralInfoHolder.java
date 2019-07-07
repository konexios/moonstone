package com.arrow.selene.device.peripheral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.arrow.acs.JsonUtils;

public class PeripheralInfoHolder implements Serializable {
	private static final long serialVersionUID = -1250770666985849003L;

	private Map<String, Map<String, String>> devices = new HashMap<>();

	public Map<String, Map<String, String>> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, Map<String, String>> devices) {
		this.devices = devices;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
